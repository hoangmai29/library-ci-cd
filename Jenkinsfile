pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven 3.8.6'
        JAVA_HOME = tool 'JDK 1.8'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & SonarQube') {
            steps {
                withCredentials([string(credentialsId: 'mysonar-token', variable: 'SONAR_TOKEN')]) {
                    bat "\"${MAVEN_HOME}\\bin\\mvn.cmd\" clean verify sonar:sonar -Dsonar.projectKey=library-ci-cd -Dsonar.host.url=http://localhost:9000 -Dsonar.token=%SONAR_TOKEN%"
                }
            }
        }

        stage('Package') {
            steps {
                bat "\"${MAVEN_HOME}\\bin\\mvn.cmd\" package"
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true

            // Nếu bạn chưa có test, có thể tạm comment dòng này
            // Nếu bạn có test thì giữ nguyên
            // junit 'target/surefire-reports/*.xml'
        }
        success {
            echo '✅ Build succeeded!'
        }
        failure {
            echo '❌ Build failed!'
        }
    }
}
