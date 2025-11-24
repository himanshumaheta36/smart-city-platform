@echo off
echo === Generation des classes gRPC ===

REM Créer le dossier de sortie
mkdir target\generated-sources\protobuf\java 2>nul

REM Générer les classes avec Docker
docker run --rm -v "%CD%:/workspace" -w /workspace namely/protoc-all:1.51_2 -f src/main/proto/emergency.proto -l java -o target/generated-sources/protobuf/java

echo === Generation terminee ===
echo Les classes sont dans target/generated-sources/protobuf/java/