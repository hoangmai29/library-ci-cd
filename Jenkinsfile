pipeline {
    agent any

    tools {
        maven 'Maven 3.9.6'   // Đúng tên bạn đã cấu hình trong Jenkins
        jdk 'JDK 17'          // Đúng tên đã khai báo trong Jenkins
    }

    environment {
        SONAR_TOKEN = credentials('sonarqube-token')
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "mvn clean verify sonar:sonar"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate()
                }
            }
        }

        stage('Docker Build') {
            steps {
                bat 'docker build -t library-app .'
            }
        }

        stage('Docker Compose Deploy') {
            steps {
                bat 'docker-compose up -d'
            }
        }

        stage('SSH Deploy') {
            steps {
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'deploy-server',
                            transfers: [
                                sshTransfer(
                                    sourceFiles: '**/*',
                                    removePrefix: '',
                                    remoteDirectory: '/home/ubuntu/deploy',
                                    execCommand: 'docker-compose down && docker-compose up -d'
                                )
                            ],
                            usePromotionTimestamp: false
                        )
                    ]
                )
            }
        }
    }
}
