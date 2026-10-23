pipeline {
    agent any
    tools { jdk 'java21'; maven 'maven3' }
    stages {
        stage('Build and unit tests') { steps { sh 'mvn -B clean verify' } }
        stage('Static analysis') { steps { sh 'mvn -B -DskipTests validate' } }
        stage('Package') { steps { archiveArtifacts artifacts: 'target/*.jar', fingerprint: true } }
        stage('Deploy approval') { steps { input message: 'Deploy only outside operating hours?' } }
    }
}
