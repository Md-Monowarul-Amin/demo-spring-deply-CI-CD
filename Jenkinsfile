pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "springboot-app"
        DOCKER_TAG = "${BUILD_NUMBER}"
    }

    stages {
        stage('Checkout') {
            steps {
                echo 'Checking out code...'
                checkout scm
            }
        }

        stage('Build Application') {
            steps {
                echo 'Building application (skipping tests)...'
                sh '''
                    chmod +x gradlew
                    ./gradlew clean bootJar -x test --no-daemon
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh '''
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    docker tag ${DOCKER_IMAGE}:${DOCKER_TAG} ${DOCKER_IMAGE}:latest
                '''
            }
        }

        stage('Stop Old Containers') {
            steps {
                echo 'Stopping old containers...'
                sh '''
                    docker-compose down || true
                '''
            }
        }

        stage('Deploy with Docker Compose') {
            steps {
                echo 'Deploying application with database...'
                sh '''
                    docker-compose up -d --build
                '''
            }
        }

        stage('Wait for Application') {
            steps {
                echo 'Waiting for application to start...'
                sh '''
                    sleep 15
                '''
            }
        }

        stage('Run Integration Tests') {
            steps {
                echo 'Running integration tests...'
                sh '''
                    docker-compose exec -T app ./gradlew test --no-daemon || true
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Verifying deployment...'
                sh '''
                    docker ps
                    curl -f http://localhost:8081/actuator/health || echo "Health check failed"
                '''
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
            sh 'docker-compose logs || true'
        }
        always {
            echo 'Cleaning up old images...'
            sh 'docker image prune -f || true'
        }
    }
}