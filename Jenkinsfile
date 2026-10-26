pipeline {
    agent any
    tools { jdk 'jdk21'; maven 'maven3' }
    stages {
        stage('Build and tests') {
            steps { sh 'mvn -B clean verify' }
        }
        stage('Static analysis') {
            steps { sh 'mvn -B checkstyle:check -Dcheckstyle.failOnViolation=false' }
        }
        stage('Package') {
            steps { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true }
        }
        stage('Production approval') {
            when { branch 'main' }
            steps { input message: 'Confirmar ventana de despliegue fuera del horario operativo' }
        }
    }
}
