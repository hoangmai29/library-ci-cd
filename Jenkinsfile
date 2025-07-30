pipeline {
    agent any

    tools {
        maven 'Maven'     // Kiểm tra tên đã khai báo trong Jenkins Global Tool
        jdk 'JDK17'       // Tên JDK đúng với Jenkins config
    }

    environment {
        SONAR_TOKEN = credentials('sonarqube-token') // Đảm bảo đã tạo với ID này
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scm
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
            bat '''
                mvn clean verify sonar:sonar ^
                -Dsonar.projectKey=library-ci-cd ^
                -Dsonar.host.url=%SONAR_HOST_URL% ^
                -Dsonar.login=%SONAR_AUTH_TOKEN%
            '''
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
                bat 'docker-compose down || exit 0'
                bat 'docker-compose up -d'
            }
        }

        stage('SSH Deploy to Remote Server') {
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
