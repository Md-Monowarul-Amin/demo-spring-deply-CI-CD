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

        stage('Build & Test') {
            steps {
                echo 'Building and testing application...'
                sh '''
                    chmod +x gradlew
                    ./gradlew clean build --no-daemon
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

        stage('Deploy') {
            steps {
                echo 'Deploying application...'
                sh '''
                    docker-compose up -d
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                echo 'Verifying deployment...'
                sh '''
                    sleep 10
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
            sh 'docker-compose logs'
        }
        always {
            echo 'Cleaning up...'
            sh 'docker image prune -f'
        }
    }
}