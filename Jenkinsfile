pipeline {
    agent any
    options {
        timestamps()
        disableConcurrentBuilds()
    }
    stages {
        stage('Compilar') {
            steps {
                sh 'mvn -B -DskipTests package'
            }
        }
        stage('Pruebas y arquitectura') {
            steps {
                sh 'mvn -B verify'
            }
        }
        stage('Preparar artefacto') {
            steps {
                stash name: 'sistema-core-jar', includes: 'target/sistema-core-0.1.0-SNAPSHOT.jar'
                archiveArtifacts artifacts: 'target/sistema-core-0.1.0-SNAPSHOT.jar', fingerprint: true
            }
        }
        stage('Desplegar fuera del horario operativo') {
            when {
                branch 'main'
            }
            input {
                message '¿Está aprobada la ventana de despliegue fuera del horario operativo?'
                ok 'Desplegar'
            }
            agent {
                label 'rhel-deploy'
            }
            steps {
                unstash 'sistema-core-jar'
                sh 'sudo install -m 0644 target/sistema-core-0.1.0-SNAPSHOT.jar /opt/sistema-core/sistema-core.jar && sudo systemctl restart sistema-core && sudo systemctl is-active --quiet sistema-core'
            }
        }
    }
}
