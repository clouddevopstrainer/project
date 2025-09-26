pipeline {
  agent any

  environment {
    IMAGE_NAME = "springboot-mongo"
    DOCKERHUB_USER = "YOUR_DOCKERHUB_USER"
  }

  stages {
    stage('Checkout') {
      steps { git url: 'https://github.com/YOUR/repo.git' }
    }

    stage('Build (Maven)') {
      steps { sh 'mvn -B -DskipTests package' }
    }

    stage('Run Unit Tests') {
      steps { sh 'mvn test' }
    }

    stage('Build & Push Docker image') {
      steps {
        withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
          sh 'docker build -t $DOCKER_USER/$IMAGE_NAME:${BUILD_NUMBER} .'
          sh 'echo $DOCKER_PASS | docker login -u $DOCKER_USER --password-stdin'
          sh 'docker push $DOCKER_USER/$IMAGE_NAME:${BUILD_NUMBER}'
        }
      }
    }

    stage('Deploy to Kubernetes') {
      steps {
        withCredentials([file(credentialsId: 'kubeconfig', variable: 'KUBE_CONFIG_FILE')]) {
          sh '''
            mkdir -p $HOME/.kube
            cp $KUBE_CONFIG_FILE $HOME/.kube/config
            kubectl set image deployment/springboot-app springboot-app=$DOCKER_USER/$IMAGE_NAME:${BUILD_NUMBER} --namespace=default || kubectl apply -f k8s/
          '''
        }
      }
    }
  }

  post {
    always {
      archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
    }
  }
}
