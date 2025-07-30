pipeline {
    agent any

    environment {
        MAVEN_HOME = tool 'Maven 3.8.6'           // Tên Maven trong Jenkins
        JAVA_HOME = tool 'JDK 17'                 // Trỏ đến: C:\Program Files\Java\jdk-17
        PATH = "${JAVA_HOME}\\bin;${env.PATH}"    // Đảm bảo gọi được java
    }

    stages {

        stage('Clean Workspace') {
            steps {
                deleteDir()  // Xoá toàn bộ file trong workspace Jenkins
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build & SonarQube') {
            steps {
                withCredentials([string(credentialsId: 'mysonar-token', variable: 'SONAR_TOKEN')]) {
                    bat "\"${MAVEN_HOME}\\bin\\mvn.cmd\" clean install -DskipTests=true sonar:sonar -Dsonar.projectKey=library-ci-cd -Dsonar.host.url=http://localhost:9000 -Dsonar.token=%SONAR_TOKEN%"
                }
            }
        }

        stage('Package') {
            steps {
                bat "\"${MAVEN_HOME}\\bin\\mvn.cmd\" package"
            }
        }

        stage('Build Docker Image') {
            steps {
                bat 'docker build -t library-app .'
            }
        }

        stage('Run Docker Container') {
            steps {
                // Stop and remove old container if exists
                bat 'docker stop library-container || echo "No running container"'
                bat 'docker rm library-container || echo "No existing container"'

                // Run new container
                bat 'docker run -d -p 9090:8080 --name library-container library-app'
            }
        }

        stage('Deploy to Server') {
            steps {
                sshagent(credentials: ['my-ssh-credentials']) {
                    sh 'ssh user@server "cd /app && docker-compose down && git pull && docker-compose up -d --build"'
                }
            }
        }
    }

    post {
        always {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            junit 'target/surefire-reports/*.xml'
        }

        success {
            echo '✅ Build succeeded!'
        }

        failure {
            echo '❌ Build failed!'
        }
    }
}
