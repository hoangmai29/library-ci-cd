pipeline {
    agent any

    tools {
        maven 'Maven'     // Đảm bảo tên này khớp với cấu hình Global Tool trong Jenkins
        jdk 'JDK17'       // Kiểm tra lại tên JDK đã khai báo trong Jenkins Global Tool
    }

    environment {
        SONAR_TOKEN = credentials('sonarqube-token') // Phải tạo Credentials ID này trong Jenkins trước
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scm // Nếu bạn đang dùng multibranch pipeline hoặc SCM đã cấu hình sẵn
            }
        }

        stage('Build') {
            steps {
                bat 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    bat "mvn clean verify sonar:sonar -Dsonar.token=${SONAR_TOKEN}"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
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
                bat 'docker-compose down || exit 0' // Tắt container cũ nếu đang chạy
                bat 'docker-compose up -d'
            }
        }

        stage('SSH Deploy to Remote Server') {
            steps {
                sshPublisher(
                    publishers: [
                        sshPublisherDesc(
                            configName: 'deploy-server', // Đặt đúng tên cấu hình trong Jenkins > Configure System > Publish Over SSH
                            transfers: [
                                sshTransfer(
                                    sourceFiles: '**/*',
                                    removePrefix: '',
                                    remoteDirectory: '/home/ubuntu/deploy',
                                    execCommand: '''
                                        cd /home/ubuntu/deploy
                                        docker-compose down || true
                                        docker-compose up -d
                                    '''
                                )
                            ],
                            usePromotionTimestamp: false,
                            verbose: true
                        )
                    ]
                )
            }
        }
    }
}
