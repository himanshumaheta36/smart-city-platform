package com.smartcity.emergency.grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * Service de gestion des urgences
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.61.0)",
    comments = "Source: emergency.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class EmergencyServiceGrpc {

  private EmergencyServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "emergency.EmergencyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.CreateEmergencyRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getCreateEmergencyAlertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "CreateEmergencyAlert",
      requestType = com.smartcity.emergency.grpc.CreateEmergencyRequest.class,
      responseType = com.smartcity.emergency.grpc.EmergencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.CreateEmergencyRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getCreateEmergencyAlertMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.CreateEmergencyRequest, com.smartcity.emergency.grpc.EmergencyResponse> getCreateEmergencyAlertMethod;
    if ((getCreateEmergencyAlertMethod = EmergencyServiceGrpc.getCreateEmergencyAlertMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getCreateEmergencyAlertMethod = EmergencyServiceGrpc.getCreateEmergencyAlertMethod) == null) {
          EmergencyServiceGrpc.getCreateEmergencyAlertMethod = getCreateEmergencyAlertMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.CreateEmergencyRequest, com.smartcity.emergency.grpc.EmergencyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "CreateEmergencyAlert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.CreateEmergencyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.EmergencyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("CreateEmergencyAlert"))
              .build();
        }
      }
    }
    return getCreateEmergencyAlertMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.GetEmergencyRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getGetEmergencyAlertMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetEmergencyAlert",
      requestType = com.smartcity.emergency.grpc.GetEmergencyRequest.class,
      responseType = com.smartcity.emergency.grpc.EmergencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.GetEmergencyRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getGetEmergencyAlertMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.GetEmergencyRequest, com.smartcity.emergency.grpc.EmergencyResponse> getGetEmergencyAlertMethod;
    if ((getGetEmergencyAlertMethod = EmergencyServiceGrpc.getGetEmergencyAlertMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getGetEmergencyAlertMethod = EmergencyServiceGrpc.getGetEmergencyAlertMethod) == null) {
          EmergencyServiceGrpc.getGetEmergencyAlertMethod = getGetEmergencyAlertMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.GetEmergencyRequest, com.smartcity.emergency.grpc.EmergencyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetEmergencyAlert"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.GetEmergencyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.EmergencyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("GetEmergencyAlert"))
              .build();
        }
      }
    }
    return getGetEmergencyAlertMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.UpdateEmergencyRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getUpdateEmergencyStatusMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "UpdateEmergencyStatus",
      requestType = com.smartcity.emergency.grpc.UpdateEmergencyRequest.class,
      responseType = com.smartcity.emergency.grpc.EmergencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.UpdateEmergencyRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getUpdateEmergencyStatusMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.UpdateEmergencyRequest, com.smartcity.emergency.grpc.EmergencyResponse> getUpdateEmergencyStatusMethod;
    if ((getUpdateEmergencyStatusMethod = EmergencyServiceGrpc.getUpdateEmergencyStatusMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getUpdateEmergencyStatusMethod = EmergencyServiceGrpc.getUpdateEmergencyStatusMethod) == null) {
          EmergencyServiceGrpc.getUpdateEmergencyStatusMethod = getUpdateEmergencyStatusMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.UpdateEmergencyRequest, com.smartcity.emergency.grpc.EmergencyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "UpdateEmergencyStatus"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.UpdateEmergencyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.EmergencyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("UpdateEmergencyStatus"))
              .build();
        }
      }
    }
    return getUpdateEmergencyStatusMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.ListEmergenciesRequest,
      com.smartcity.emergency.grpc.ListEmergenciesResponse> getListActiveEmergenciesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListActiveEmergencies",
      requestType = com.smartcity.emergency.grpc.ListEmergenciesRequest.class,
      responseType = com.smartcity.emergency.grpc.ListEmergenciesResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.ListEmergenciesRequest,
      com.smartcity.emergency.grpc.ListEmergenciesResponse> getListActiveEmergenciesMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.ListEmergenciesRequest, com.smartcity.emergency.grpc.ListEmergenciesResponse> getListActiveEmergenciesMethod;
    if ((getListActiveEmergenciesMethod = EmergencyServiceGrpc.getListActiveEmergenciesMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getListActiveEmergenciesMethod = EmergencyServiceGrpc.getListActiveEmergenciesMethod) == null) {
          EmergencyServiceGrpc.getListActiveEmergenciesMethod = getListActiveEmergenciesMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.ListEmergenciesRequest, com.smartcity.emergency.grpc.ListEmergenciesResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListActiveEmergencies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.ListEmergenciesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.ListEmergenciesResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("ListActiveEmergencies"))
              .build();
        }
      }
    }
    return getListActiveEmergenciesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.EmergencyStreamRequest,
      com.smartcity.emergency.grpc.EmergencyStreamResponse> getStreamEmergenciesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "StreamEmergencies",
      requestType = com.smartcity.emergency.grpc.EmergencyStreamRequest.class,
      responseType = com.smartcity.emergency.grpc.EmergencyStreamResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.EmergencyStreamRequest,
      com.smartcity.emergency.grpc.EmergencyStreamResponse> getStreamEmergenciesMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.EmergencyStreamRequest, com.smartcity.emergency.grpc.EmergencyStreamResponse> getStreamEmergenciesMethod;
    if ((getStreamEmergenciesMethod = EmergencyServiceGrpc.getStreamEmergenciesMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getStreamEmergenciesMethod = EmergencyServiceGrpc.getStreamEmergenciesMethod) == null) {
          EmergencyServiceGrpc.getStreamEmergenciesMethod = getStreamEmergenciesMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.EmergencyStreamRequest, com.smartcity.emergency.grpc.EmergencyStreamResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "StreamEmergencies"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.EmergencyStreamRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.EmergencyStreamResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("StreamEmergencies"))
              .build();
        }
      }
    }
    return getStreamEmergenciesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.AssignResourcesRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getAssignResourcesMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AssignResources",
      requestType = com.smartcity.emergency.grpc.AssignResourcesRequest.class,
      responseType = com.smartcity.emergency.grpc.EmergencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.AssignResourcesRequest,
      com.smartcity.emergency.grpc.EmergencyResponse> getAssignResourcesMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.AssignResourcesRequest, com.smartcity.emergency.grpc.EmergencyResponse> getAssignResourcesMethod;
    if ((getAssignResourcesMethod = EmergencyServiceGrpc.getAssignResourcesMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getAssignResourcesMethod = EmergencyServiceGrpc.getAssignResourcesMethod) == null) {
          EmergencyServiceGrpc.getAssignResourcesMethod = getAssignResourcesMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.AssignResourcesRequest, com.smartcity.emergency.grpc.EmergencyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AssignResources"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.AssignResourcesRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.EmergencyResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("AssignResources"))
              .build();
        }
      }
    }
    return getAssignResourcesMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.StatsRequest,
      com.smartcity.emergency.grpc.StatsResponse> getGetEmergencyStatsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetEmergencyStats",
      requestType = com.smartcity.emergency.grpc.StatsRequest.class,
      responseType = com.smartcity.emergency.grpc.StatsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.StatsRequest,
      com.smartcity.emergency.grpc.StatsResponse> getGetEmergencyStatsMethod() {
    io.grpc.MethodDescriptor<com.smartcity.emergency.grpc.StatsRequest, com.smartcity.emergency.grpc.StatsResponse> getGetEmergencyStatsMethod;
    if ((getGetEmergencyStatsMethod = EmergencyServiceGrpc.getGetEmergencyStatsMethod) == null) {
      synchronized (EmergencyServiceGrpc.class) {
        if ((getGetEmergencyStatsMethod = EmergencyServiceGrpc.getGetEmergencyStatsMethod) == null) {
          EmergencyServiceGrpc.getGetEmergencyStatsMethod = getGetEmergencyStatsMethod =
              io.grpc.MethodDescriptor.<com.smartcity.emergency.grpc.StatsRequest, com.smartcity.emergency.grpc.StatsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetEmergencyStats"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.StatsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.smartcity.emergency.grpc.StatsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new EmergencyServiceMethodDescriptorSupplier("GetEmergencyStats"))
              .build();
        }
      }
    }
    return getGetEmergencyStatsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static EmergencyServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EmergencyServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EmergencyServiceStub>() {
        @java.lang.Override
        public EmergencyServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EmergencyServiceStub(channel, callOptions);
        }
      };
    return EmergencyServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static EmergencyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EmergencyServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EmergencyServiceBlockingStub>() {
        @java.lang.Override
        public EmergencyServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EmergencyServiceBlockingStub(channel, callOptions);
        }
      };
    return EmergencyServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static EmergencyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<EmergencyServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<EmergencyServiceFutureStub>() {
        @java.lang.Override
        public EmergencyServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new EmergencyServiceFutureStub(channel, callOptions);
        }
      };
    return EmergencyServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * Service de gestion des urgences
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * Créer une nouvelle alerte d'urgence
     * </pre>
     */
    default void createEmergencyAlert(com.smartcity.emergency.grpc.CreateEmergencyRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getCreateEmergencyAlertMethod(), responseObserver);
    }

    /**
     * <pre>
     * Obtenir les détails d'une alerte
     * </pre>
     */
    default void getEmergencyAlert(com.smartcity.emergency.grpc.GetEmergencyRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetEmergencyAlertMethod(), responseObserver);
    }

    /**
     * <pre>
     * Mettre à jour le statut d'une alerte
     * </pre>
     */
    default void updateEmergencyStatus(com.smartcity.emergency.grpc.UpdateEmergencyRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getUpdateEmergencyStatusMethod(), responseObserver);
    }

    /**
     * <pre>
     * Lister toutes les alertes actives
     * </pre>
     */
    default void listActiveEmergencies(com.smartcity.emergency.grpc.ListEmergenciesRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.ListEmergenciesResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListActiveEmergenciesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Stream des alertes en temps réel (bidirectionnel)
     * </pre>
     */
    default io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyStreamRequest> streamEmergencies(
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyStreamResponse> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getStreamEmergenciesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Assigner des ressources à une urgence
     * </pre>
     */
    default void assignResources(com.smartcity.emergency.grpc.AssignResourcesRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAssignResourcesMethod(), responseObserver);
    }

    /**
     * <pre>
     * Obtenir les statistiques des urgences
     * </pre>
     */
    default void getEmergencyStats(com.smartcity.emergency.grpc.StatsRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.StatsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetEmergencyStatsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service EmergencyService.
   * <pre>
   * Service de gestion des urgences
   * </pre>
   */
  public static abstract class EmergencyServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return EmergencyServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service EmergencyService.
   * <pre>
   * Service de gestion des urgences
   * </pre>
   */
  public static final class EmergencyServiceStub
      extends io.grpc.stub.AbstractAsyncStub<EmergencyServiceStub> {
    private EmergencyServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmergencyServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EmergencyServiceStub(channel, callOptions);
    }

    /**
     * <pre>
     * Créer une nouvelle alerte d'urgence
     * </pre>
     */
    public void createEmergencyAlert(com.smartcity.emergency.grpc.CreateEmergencyRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getCreateEmergencyAlertMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Obtenir les détails d'une alerte
     * </pre>
     */
    public void getEmergencyAlert(com.smartcity.emergency.grpc.GetEmergencyRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetEmergencyAlertMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Mettre à jour le statut d'une alerte
     * </pre>
     */
    public void updateEmergencyStatus(com.smartcity.emergency.grpc.UpdateEmergencyRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getUpdateEmergencyStatusMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Lister toutes les alertes actives
     * </pre>
     */
    public void listActiveEmergencies(com.smartcity.emergency.grpc.ListEmergenciesRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.ListEmergenciesResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getListActiveEmergenciesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Stream des alertes en temps réel (bidirectionnel)
     * </pre>
     */
    public io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyStreamRequest> streamEmergencies(
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyStreamResponse> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getStreamEmergenciesMethod(), getCallOptions()), responseObserver);
    }

    /**
     * <pre>
     * Assigner des ressources à une urgence
     * </pre>
     */
    public void assignResources(com.smartcity.emergency.grpc.AssignResourcesRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAssignResourcesMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     * <pre>
     * Obtenir les statistiques des urgences
     * </pre>
     */
    public void getEmergencyStats(com.smartcity.emergency.grpc.StatsRequest request,
        io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.StatsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetEmergencyStatsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service EmergencyService.
   * <pre>
   * Service de gestion des urgences
   * </pre>
   */
  public static final class EmergencyServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<EmergencyServiceBlockingStub> {
    private EmergencyServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmergencyServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EmergencyServiceBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * Créer une nouvelle alerte d'urgence
     * </pre>
     */
    public com.smartcity.emergency.grpc.EmergencyResponse createEmergencyAlert(com.smartcity.emergency.grpc.CreateEmergencyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getCreateEmergencyAlertMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Obtenir les détails d'une alerte
     * </pre>
     */
    public com.smartcity.emergency.grpc.EmergencyResponse getEmergencyAlert(com.smartcity.emergency.grpc.GetEmergencyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetEmergencyAlertMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Mettre à jour le statut d'une alerte
     * </pre>
     */
    public com.smartcity.emergency.grpc.EmergencyResponse updateEmergencyStatus(com.smartcity.emergency.grpc.UpdateEmergencyRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getUpdateEmergencyStatusMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Lister toutes les alertes actives
     * </pre>
     */
    public com.smartcity.emergency.grpc.ListEmergenciesResponse listActiveEmergencies(com.smartcity.emergency.grpc.ListEmergenciesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getListActiveEmergenciesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Assigner des ressources à une urgence
     * </pre>
     */
    public com.smartcity.emergency.grpc.EmergencyResponse assignResources(com.smartcity.emergency.grpc.AssignResourcesRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAssignResourcesMethod(), getCallOptions(), request);
    }

    /**
     * <pre>
     * Obtenir les statistiques des urgences
     * </pre>
     */
    public com.smartcity.emergency.grpc.StatsResponse getEmergencyStats(com.smartcity.emergency.grpc.StatsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetEmergencyStatsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service EmergencyService.
   * <pre>
   * Service de gestion des urgences
   * </pre>
   */
  public static final class EmergencyServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<EmergencyServiceFutureStub> {
    private EmergencyServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected EmergencyServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new EmergencyServiceFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * Créer une nouvelle alerte d'urgence
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartcity.emergency.grpc.EmergencyResponse> createEmergencyAlert(
        com.smartcity.emergency.grpc.CreateEmergencyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getCreateEmergencyAlertMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Obtenir les détails d'une alerte
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartcity.emergency.grpc.EmergencyResponse> getEmergencyAlert(
        com.smartcity.emergency.grpc.GetEmergencyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetEmergencyAlertMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Mettre à jour le statut d'une alerte
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartcity.emergency.grpc.EmergencyResponse> updateEmergencyStatus(
        com.smartcity.emergency.grpc.UpdateEmergencyRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getUpdateEmergencyStatusMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Lister toutes les alertes actives
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartcity.emergency.grpc.ListEmergenciesResponse> listActiveEmergencies(
        com.smartcity.emergency.grpc.ListEmergenciesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getListActiveEmergenciesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Assigner des ressources à une urgence
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartcity.emergency.grpc.EmergencyResponse> assignResources(
        com.smartcity.emergency.grpc.AssignResourcesRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAssignResourcesMethod(), getCallOptions()), request);
    }

    /**
     * <pre>
     * Obtenir les statistiques des urgences
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<com.smartcity.emergency.grpc.StatsResponse> getEmergencyStats(
        com.smartcity.emergency.grpc.StatsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetEmergencyStatsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_CREATE_EMERGENCY_ALERT = 0;
  private static final int METHODID_GET_EMERGENCY_ALERT = 1;
  private static final int METHODID_UPDATE_EMERGENCY_STATUS = 2;
  private static final int METHODID_LIST_ACTIVE_EMERGENCIES = 3;
  private static final int METHODID_ASSIGN_RESOURCES = 4;
  private static final int METHODID_GET_EMERGENCY_STATS = 5;
  private static final int METHODID_STREAM_EMERGENCIES = 6;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_CREATE_EMERGENCY_ALERT:
          serviceImpl.createEmergencyAlert((com.smartcity.emergency.grpc.CreateEmergencyRequest) request,
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse>) responseObserver);
          break;
        case METHODID_GET_EMERGENCY_ALERT:
          serviceImpl.getEmergencyAlert((com.smartcity.emergency.grpc.GetEmergencyRequest) request,
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse>) responseObserver);
          break;
        case METHODID_UPDATE_EMERGENCY_STATUS:
          serviceImpl.updateEmergencyStatus((com.smartcity.emergency.grpc.UpdateEmergencyRequest) request,
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse>) responseObserver);
          break;
        case METHODID_LIST_ACTIVE_EMERGENCIES:
          serviceImpl.listActiveEmergencies((com.smartcity.emergency.grpc.ListEmergenciesRequest) request,
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.ListEmergenciesResponse>) responseObserver);
          break;
        case METHODID_ASSIGN_RESOURCES:
          serviceImpl.assignResources((com.smartcity.emergency.grpc.AssignResourcesRequest) request,
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyResponse>) responseObserver);
          break;
        case METHODID_GET_EMERGENCY_STATS:
          serviceImpl.getEmergencyStats((com.smartcity.emergency.grpc.StatsRequest) request,
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.StatsResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_STREAM_EMERGENCIES:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.streamEmergencies(
              (io.grpc.stub.StreamObserver<com.smartcity.emergency.grpc.EmergencyStreamResponse>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getCreateEmergencyAlertMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.CreateEmergencyRequest,
              com.smartcity.emergency.grpc.EmergencyResponse>(
                service, METHODID_CREATE_EMERGENCY_ALERT)))
        .addMethod(
          getGetEmergencyAlertMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.GetEmergencyRequest,
              com.smartcity.emergency.grpc.EmergencyResponse>(
                service, METHODID_GET_EMERGENCY_ALERT)))
        .addMethod(
          getUpdateEmergencyStatusMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.UpdateEmergencyRequest,
              com.smartcity.emergency.grpc.EmergencyResponse>(
                service, METHODID_UPDATE_EMERGENCY_STATUS)))
        .addMethod(
          getListActiveEmergenciesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.ListEmergenciesRequest,
              com.smartcity.emergency.grpc.ListEmergenciesResponse>(
                service, METHODID_LIST_ACTIVE_EMERGENCIES)))
        .addMethod(
          getStreamEmergenciesMethod(),
          io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.EmergencyStreamRequest,
              com.smartcity.emergency.grpc.EmergencyStreamResponse>(
                service, METHODID_STREAM_EMERGENCIES)))
        .addMethod(
          getAssignResourcesMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.AssignResourcesRequest,
              com.smartcity.emergency.grpc.EmergencyResponse>(
                service, METHODID_ASSIGN_RESOURCES)))
        .addMethod(
          getGetEmergencyStatsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.smartcity.emergency.grpc.StatsRequest,
              com.smartcity.emergency.grpc.StatsResponse>(
                service, METHODID_GET_EMERGENCY_STATS)))
        .build();
  }

  private static abstract class EmergencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    EmergencyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.smartcity.emergency.grpc.EmergencyProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("EmergencyService");
    }
  }

  private static final class EmergencyServiceFileDescriptorSupplier
      extends EmergencyServiceBaseDescriptorSupplier {
    EmergencyServiceFileDescriptorSupplier() {}
  }

  private static final class EmergencyServiceMethodDescriptorSupplier
      extends EmergencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    EmergencyServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (EmergencyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new EmergencyServiceFileDescriptorSupplier())
              .addMethod(getCreateEmergencyAlertMethod())
              .addMethod(getGetEmergencyAlertMethod())
              .addMethod(getUpdateEmergencyStatusMethod())
              .addMethod(getListActiveEmergenciesMethod())
              .addMethod(getStreamEmergenciesMethod())
              .addMethod(getAssignResourcesMethod())
              .addMethod(getGetEmergencyStatsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
