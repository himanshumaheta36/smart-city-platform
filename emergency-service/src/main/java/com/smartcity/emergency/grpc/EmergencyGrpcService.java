package com.smartcity.emergency.grpc;

import com.smartcity.emergency.model.EmergencyAlert;
import com.smartcity.emergency.model.ResourceAssignment;
import com.smartcity.emergency.service.EmergencyService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@GrpcService
public class EmergencyGrpcService extends EmergencyServiceGrpc.EmergencyServiceImplBase {
    
    @Autowired
    private EmergencyService emergencyService;
    
    private final Map<String, List<StreamObserver<EmergencyStreamResponse>>> streamSubscribers = 
        new ConcurrentHashMap<>();
    
    private static final DateTimeFormatter DATE_FORMATTER = 
        DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
    @Override
    public void createEmergencyAlert(CreateEmergencyRequest request, 
                                   StreamObserver<EmergencyResponse> responseObserver) {
        try {
            // Convertir la requête gRPC en modèle de domaine
            List<String> tags = new ArrayList<>(request.getTagsList());
            
            EmergencyAlert alert = emergencyService.createEmergencyAlert(
                request.getReporterId(),
                convertEmergencyType(request.getEmergencyType()),
                convertSeverityLevel(request.getSeverity()),
                request.getLocation(),
                request.getLatitude(),
                request.getLongitude(),
                request.getDescription(),
                request.getAffectedPeople(),
                tags
            );
            
            // Convertir la réponse en gRPC
            EmergencyResponse response = convertToGrpcResponse(alert);
            responseObserver.onNext(response);
            responseObserver.onCompleted();
            
            // Notifier les abonnés du stream
            notifyStreamSubscribers(alert, StreamAction.STREAM_CREATED);
            
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Failed to create emergency alert: " + e.getMessage())
                .asRuntimeException());
        }
    }
    
    @Override
    public void getEmergencyAlert(GetEmergencyRequest request, 
                                StreamObserver<EmergencyResponse> responseObserver) {
        try {
            Optional<EmergencyAlert> alert = emergencyService.getEmergencyAlert(request.getEmergencyId());
            
            if (alert.isPresent()) {
                EmergencyResponse response = convertToGrpcResponse(alert.get());
                responseObserver.onNext(response);
                responseObserver.onCompleted();
            } else {
                responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Emergency alert not found: " + request.getEmergencyId())
                    .asRuntimeException());
            }
            
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Failed to get emergency alert: " + e.getMessage())
                .asRuntimeException());
        }
    }
    
    @Override
    public void updateEmergencyStatus(UpdateEmergencyRequest request, 
                                    StreamObserver<EmergencyResponse> responseObserver) {
        try {
            Optional<EmergencyAlert> alert = emergencyService.updateEmergencyStatus(
                request.getEmergencyId(),
                convertEmergencyStatus(request.getStatus()),
                request.getResponderId(),
                request.getUpdateNotes()
            );
            
            if (alert.isPresent()) {
                EmergencyResponse response = convertToGrpcResponse(alert.get());
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                
                // Notifier les abonnés du stream
                notifyStreamSubscribers(alert.get(), StreamAction.STREAM_UPDATED);
            } else {
                responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Emergency alert not found: " + request.getEmergencyId())
                    .asRuntimeException());
            }
            
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Failed to update emergency status: " + e.getMessage())
                .asRuntimeException());
        }
    }
    
    @Override
    public void listActiveEmergencies(ListEmergenciesRequest request, 
                                    StreamObserver<ListEmergenciesResponse> responseObserver) {
        try {
            List<EmergencyAlert> emergencies;
            
            if (request.getTypeFilter() != EmergencyType.UNKNOWN_EMERGENCY ||
                request.getSeverityFilter() != SeverityLevel.LOW_SEVERITY ||
                request.getStatusFilter() != EmergencyStatus.REPORTED_STATUS ||
                !request.getLocationFilter().isEmpty()) {
                
                // Filtrer les urgences
                emergencies = emergencyService.filterEmergencies(
                    request.getTypeFilter() == EmergencyType.UNKNOWN_EMERGENCY ? null : convertEmergencyType(request.getTypeFilter()),
                    request.getSeverityFilter() == SeverityLevel.LOW_SEVERITY ? null : convertSeverityLevel(request.getSeverityFilter()),
                    request.getStatusFilter() == EmergencyStatus.REPORTED_STATUS ? null : convertEmergencyStatus(request.getStatusFilter()),
                    request.getLocationFilter().isEmpty() ? null : request.getLocationFilter()
                );
            } else {
                // Toutes les urgences actives
                emergencies = emergencyService.getActiveEmergencies();
            }
            
            ListEmergenciesResponse.Builder responseBuilder = ListEmergenciesResponse.newBuilder();
            
            for (EmergencyAlert alert : emergencies) {
                responseBuilder.addEmergencies(convertToGrpcResponse(alert));
            }
            
            responseBuilder.setTotalCount(emergencies.size());
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Failed to list emergencies: " + e.getMessage())
                .asRuntimeException());
        }
    }
    
    @Override
    public void assignResources(AssignResourcesRequest request, 
                              StreamObserver<EmergencyResponse> responseObserver) {
        try {
            List<ResourceAssignment> resources = new ArrayList<>();
            
            for (com.smartcity.emergency.grpc.ResourceAssignment grpcResource : request.getResourcesList()) {
                ResourceAssignment resource = new ResourceAssignment(
                    convertResourceType(grpcResource.getResourceType()),
                    grpcResource.getResourceId(),
                    grpcResource.getQuantity(),
                    grpcResource.getEstimatedArrivalMinutes()
                );
                resources.add(resource);
            }
            
            Optional<EmergencyAlert> alert = emergencyService.assignResources(
                request.getEmergencyId(), resources);
            
            if (alert.isPresent()) {
                EmergencyResponse response = convertToGrpcResponse(alert.get());
                responseObserver.onNext(response);
                responseObserver.onCompleted();
                
                // Notifier les abonnés du stream
                notifyStreamSubscribers(alert.get(), StreamAction.STREAM_UPDATED);
            } else {
                responseObserver.onError(io.grpc.Status.NOT_FOUND
                    .withDescription("Emergency alert not found: " + request.getEmergencyId())
                    .asRuntimeException());
            }
            
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Failed to assign resources: " + e.getMessage())
                .asRuntimeException());
        }
    }
    
    @Override
    public void getEmergencyStats(StatsRequest request, 
                                StreamObserver<StatsResponse> responseObserver) {
        try {
            Map<String, Object> stats = emergencyService.getEmergencyStats(
                request.getHoursBack(),
                request.getLocationFilter().isEmpty() ? null : request.getLocationFilter()
            );
            
            StatsResponse.Builder responseBuilder = StatsResponse.newBuilder();
            
            responseBuilder.setTotalEmergencies((Integer) stats.get("totalEmergencies"));
            responseBuilder.setActiveEmergencies(((Long) stats.get("activeEmergencies")).intValue());
            responseBuilder.setAverageResponseTimeMinutes((Double) stats.get("averageResponseTimeMinutes"));
            responseBuilder.setPeriodCovered((String) stats.get("periodCovered"));
            
            // Map des types
            @SuppressWarnings("unchecked")
            Map<String, Long> byType = (Map<String, Long>) stats.get("emergenciesByType");
            for (Map.Entry<String, Long> entry : byType.entrySet()) {
                responseBuilder.putEmergenciesByType(entry.getKey(), entry.getValue().intValue());
            }
            
            // Map des sévérités
            @SuppressWarnings("unchecked")
            Map<String, Long> bySeverity = (Map<String, Long>) stats.get("emergenciesBySeverity");
            for (Map.Entry<String, Long> entry : bySeverity.entrySet()) {
                responseBuilder.putEmergenciesBySeverity(entry.getKey(), entry.getValue().intValue());
            }
            
            // Map des statuts
            @SuppressWarnings("unchecked")
            Map<String, Long> byStatus = (Map<String, Long>) stats.get("emergenciesByStatus");
            for (Map.Entry<String, Long> entry : byStatus.entrySet()) {
                responseBuilder.putEmergenciesByStatus(entry.getKey(), entry.getValue().intValue());
            }
            
            // Hotspots
            @SuppressWarnings("unchecked")
            List<String> hotspots = (List<String>) stats.get("hotspots");
            responseBuilder.addAllHotspots(hotspots);
            
            responseObserver.onNext(responseBuilder.build());
            responseObserver.onCompleted();
            
        } catch (Exception e) {
            responseObserver.onError(io.grpc.Status.INTERNAL
                .withDescription("Failed to get emergency stats: " + e.getMessage())
                .asRuntimeException());
        }
    }
    
    @Override
    public StreamObserver<EmergencyStreamRequest> streamEmergencies(
        StreamObserver<EmergencyStreamResponse> responseObserver) {
        
        return new StreamObserver<EmergencyStreamRequest>() {
            private String clientId;
            
            @Override
            public void onNext(EmergencyStreamRequest request) {
                this.clientId = request.getClientId();
                
                // Ajouter l'observateur à la liste des abonnés
                streamSubscribers.computeIfAbsent(clientId, k -> new CopyOnWriteArrayList<>())
                               .add(responseObserver);
                
                // Envoyer un message de bienvenue
                EmergencyStreamResponse welcome = EmergencyStreamResponse.newBuilder()
                    .setAction(StreamAction.STREAM_CREATED)
                    .setMessage("Connected to emergency stream as client: " + clientId)
                    .build();
                responseObserver.onNext(welcome);
            }
            
            @Override
            public void onError(Throwable t) {
                // Retirer l'observateur en cas d'erreur
                if (clientId != null) {
                    List<StreamObserver<EmergencyStreamResponse>> subscribers = streamSubscribers.get(clientId);
                    if (subscribers != null) {
                        subscribers.remove(responseObserver);
                    }
                }
            }
            
            @Override
            public void onCompleted() {
                // Retirer l'observateur quand le stream est terminé
                if (clientId != null) {
                    List<StreamObserver<EmergencyStreamResponse>> subscribers = streamSubscribers.get(clientId);
                    if (subscribers != null) {
                        subscribers.remove(responseObserver);
                    }
                }
                responseObserver.onCompleted();
            }
        };
    }
    
    // Méthodes de conversion
    private EmergencyAlert.EmergencyType convertEmergencyType(EmergencyType grpcType) {
        return switch (grpcType) {
            case ACCIDENT -> EmergencyAlert.EmergencyType.ACCIDENT;
            case FIRE -> EmergencyAlert.EmergencyType.FIRE;
            case MEDICAL -> EmergencyAlert.EmergencyType.MEDICAL;
            case SECURITY -> EmergencyAlert.EmergencyType.SECURITY;
            case NATURAL_DISASTER -> EmergencyAlert.EmergencyType.NATURAL_DISASTER;
            case TECHNICAL -> EmergencyAlert.EmergencyType.TECHNICAL;
            default -> EmergencyAlert.EmergencyType.UNKNOWN;
        };
    }
    
    private EmergencyAlert.SeverityLevel convertSeverityLevel(SeverityLevel grpcSeverity) {
        return switch (grpcSeverity) {
            case MEDIUM_SEVERITY -> EmergencyAlert.SeverityLevel.MEDIUM;
            case HIGH_SEVERITY -> EmergencyAlert.SeverityLevel.HIGH;
            case CRITICAL_SEVERITY -> EmergencyAlert.SeverityLevel.CRITICAL;
            default -> EmergencyAlert.SeverityLevel.LOW;
        };
    }
    
    private EmergencyAlert.EmergencyStatus convertEmergencyStatus(EmergencyStatus grpcStatus) {
        return switch (grpcStatus) {
            case CONFIRMED_STATUS -> EmergencyAlert.EmergencyStatus.CONFIRMED;
            case IN_PROGRESS_STATUS -> EmergencyAlert.EmergencyStatus.IN_PROGRESS;
            case RESOLVED_STATUS -> EmergencyAlert.EmergencyStatus.RESOLVED;
            case CANCELLED_STATUS -> EmergencyAlert.EmergencyStatus.CANCELLED;
            default -> EmergencyAlert.EmergencyStatus.REPORTED;
        };
    }
    
    private ResourceAssignment.ResourceType convertResourceType(ResourceType grpcResourceType) {
        return switch (grpcResourceType) {
            case FIRE_TRUCK_RESOURCE -> ResourceAssignment.ResourceType.FIRE_TRUCK;
            case POLICE_RESOURCE -> ResourceAssignment.ResourceType.POLICE;
            case RESCUE_TEAM_RESOURCE -> ResourceAssignment.ResourceType.RESCUE_TEAM;
            case HELICOPTER_RESOURCE -> ResourceAssignment.ResourceType.HELICOPTER;
            case DOCTOR_RESOURCE -> ResourceAssignment.ResourceType.DOCTOR;
            case NURSE_RESOURCE -> ResourceAssignment.ResourceType.NURSE;
            default -> ResourceAssignment.ResourceType.AMBULANCE;
        };
    }
    
    private EmergencyResponse convertToGrpcResponse(EmergencyAlert alert) {
        EmergencyResponse.Builder builder = EmergencyResponse.newBuilder()
            .setEmergencyId(alert.getEmergencyId())
            .setReporterId(alert.getReporterId())
            .setEmergencyType(convertGrpcEmergencyType(alert.getEmergencyType()))
            .setSeverity(convertGrpcSeverityLevel(alert.getSeverityLevel()))
            .setLocation(alert.getLocation())
            .setLatitude(alert.getLatitude())
            .setLongitude(alert.getLongitude())
            .setDescription(alert.getDescription())
            .setAffectedPeople(alert.getAffectedPeople())
            .setStatus(convertGrpcEmergencyStatus(alert.getStatus()))
            .setCreatedAt(DATE_FORMATTER.format(alert.getCreatedAt()))
            .setUpdatedAt(DATE_FORMATTER.format(alert.getUpdatedAt()));
        
        if (alert.getResponderId() != null) {
            builder.setResponderId(alert.getResponderId());
        }
        
        if (alert.getUpdateNotes() != null) {
            builder.setUpdateNotes(alert.getUpdateNotes());
        }
        
        // Tags
        builder.addAllTags(alert.getTags());
        
        // Resources assignées
        for (ResourceAssignment resource : alert.getAssignedResources()) {
            com.smartcity.emergency.grpc.ResourceAssignment grpcResource = 
                com.smartcity.emergency.grpc.ResourceAssignment.newBuilder()
                .setResourceType(convertGrpcResourceType(resource.getResourceType()))
                .setResourceId(resource.getResourceId())
                .setQuantity(resource.getQuantity())
                .setEstimatedArrivalMinutes(resource.getEstimatedArrivalMinutes())
                .build();
            builder.addAssignedResources(grpcResource);
        }
        
        return builder.build();
    }
    
    // Méthodes de conversion inverses
    private EmergencyType convertGrpcEmergencyType(EmergencyAlert.EmergencyType type) {
        return switch (type) {
            case ACCIDENT -> EmergencyType.ACCIDENT;
            case FIRE -> EmergencyType.FIRE;
            case MEDICAL -> EmergencyType.MEDICAL;
            case SECURITY -> EmergencyType.SECURITY;
            case NATURAL_DISASTER -> EmergencyType.NATURAL_DISASTER;
            case TECHNICAL -> EmergencyType.TECHNICAL;
            default -> EmergencyType.UNKNOWN_EMERGENCY;
        };
    }
    
    private SeverityLevel convertGrpcSeverityLevel(EmergencyAlert.SeverityLevel severity) {
        return switch (severity) {
            case MEDIUM -> SeverityLevel.MEDIUM_SEVERITY;
            case HIGH -> SeverityLevel.HIGH_SEVERITY;
            case CRITICAL -> SeverityLevel.CRITICAL_SEVERITY;
            default -> SeverityLevel.LOW_SEVERITY;
        };
    }
    
    private EmergencyStatus convertGrpcEmergencyStatus(EmergencyAlert.EmergencyStatus status) {
        return switch (status) {
            case CONFIRMED -> EmergencyStatus.CONFIRMED_STATUS;
            case IN_PROGRESS -> EmergencyStatus.IN_PROGRESS_STATUS;
            case RESOLVED -> EmergencyStatus.RESOLVED_STATUS;
            case CANCELLED -> EmergencyStatus.CANCELLED_STATUS;
            default -> EmergencyStatus.REPORTED_STATUS;
        };
    }
    
    private ResourceType convertGrpcResourceType(ResourceAssignment.ResourceType resourceType) {
        return switch (resourceType) {
            case FIRE_TRUCK -> ResourceType.FIRE_TRUCK_RESOURCE;
            case POLICE -> ResourceType.POLICE_RESOURCE;
            case RESCUE_TEAM -> ResourceType.RESCUE_TEAM_RESOURCE;
            case HELICOPTER -> ResourceType.HELICOPTER_RESOURCE;
            case DOCTOR -> ResourceType.DOCTOR_RESOURCE;
            case NURSE -> ResourceType.NURSE_RESOURCE;
            default -> ResourceType.AMBULANCE_RESOURCE;
        };
    }
    
    private void notifyStreamSubscribers(EmergencyAlert alert, StreamAction action) {
        EmergencyStreamResponse streamResponse = EmergencyStreamResponse.newBuilder()
            .setEmergency(convertToGrpcResponse(alert))
            .setAction(action)
            .setMessage("Emergency " + alert.getEmergencyId() + " " + getActionName(action))
            .build();
        
        // Notifier tous les abonnés
        for (List<StreamObserver<EmergencyStreamResponse>> subscribers : streamSubscribers.values()) {
            for (StreamObserver<EmergencyStreamResponse> subscriber : subscribers) {
                try {
                    subscriber.onNext(streamResponse);
                } catch (Exception e) {
                    // Ignorer les erreurs d'envoi
                }
            }
        }
    }
    
    private String getActionName(StreamAction action) {
        return switch (action) {
            case STREAM_CREATED -> "created";
            case STREAM_UPDATED -> "updated";
            case STREAM_RESOLVED -> "resolved";
            default -> "modified";
        };
    }
}