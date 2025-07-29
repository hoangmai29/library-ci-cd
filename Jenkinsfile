pipeline {
    agent any

    environment {
        SONAR_TOKEN = credentials('sonarqube-token')
    }

    stages {
        stage('Build') {
            steps {
                echo '🔧 Building the project...'
                bat 'mvn clean install'
            }
        }

        stage('SonarQube Analysis') {
            steps {
                echo '🔍 Running SonarQube analysis...'
                withSonarQubeEnv('SonarQube') {
                    bat "mvn clean verify sonar:sonar"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                echo '🛡️ Checking SonarQube Quality Gate...'
                timeout(time: 5, unit: 'MINUTES') {
                    waitForQualityGate(abortPipeline: true)
                }
            }
        }

        stage('Docker Build') {
            steps {
                echo '🐳 Building Docker image...'
                bat 'docker build -t library-app .'
            }
        }

        stage('Docker Compose Deploy') {
            steps {
                echo '🚀 Deploying with Docker Compose...'
                bat 'docker-compose down'
                bat 'docker-compose up -d'
            }
        }

        stage('SSH Deploy') {
            steps {
                echo '📦 Deploying via SSH to remote server...'
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
                                        docker-compose down
                                        docker-compose up -d
                                    '''
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
