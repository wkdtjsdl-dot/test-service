pipeline {
  agent {
    kubernetes {
yaml '''
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: gradle
    image: gradle:jdk21
    command: ['sleep']
    args: ['infinity']
    volumeMounts:
    - name: maven-repo
      mountPath: /root/.m2
  - name: kaniko
    image: gcr.io/kaniko-project/executor:debug
    command: ['sleep']
    args: ['infinity']
    volumeMounts:
      - name: registry-credentials
        mountPath: /kaniko/.docker
  volumes:
    - name: maven-repo
      persistentVolumeClaim:
        claimName: maven-repo-pvc
    - name: registry-credentials
      secret:
        secretName: ecr-secret
        items:
        - key: .dockerconfigjson
          path: config.json
'''
    }
  }

  parameters {
    string name: 'DEPLOY_REMOTE_NAME', defaultValue: 'k8s-worker04'
    string name: 'DEPLOY_REMOTE_HOST', defaultValue: '192.168.0.185'
    string name: 'DEPLOY_CREDENTIALS_ID', defaultValue: 'k8s-worker04-credential'
    string name: 'ACTIVE_PROFILE', defaultValue: 'dev'
    string name: 'JOOQ_URL', defaultValue: 'jdbc:postgresql://192.168.0.185:30432/ailis'
    string name: 'JOOQ_USER', defaultValue: ''
    password name: 'JOOQ_PASSWORD', defaultValue: ''
  }

  stages {
    stage ('init') {
      steps {
        container('gradle') {
          sh 'git config --global http.sslVerify false'
        }
      }
    }
    stage ('clone') {
      steps {
        echo 'Git clone!!'
        container('gradle') {
          checkout scm
        }
      }
    }
    stage ('define tag') {
      steps {
        container('gradle') {
          script {
            if (env.BUILD_NUMBER.toInteger() % 2 == 1) {
              env.tag = 'blue'
            } else {
              env.tag = 'green'
            }
            echo "tag : ${env.tag}"
          }
        }
      }
    }
    stage('build') {
      steps {
        echo 'Build step!'
        container('gradle') {
          script {
            sh 'chmod 755 gradlew'
            withEnv([
              "JOOQ_URL=${params.JOOQ_URL}",
              "JOOQ_USER=${params.JOOQ_USER}",
              "JOOQ_PASSWORD=${params.JOOQ_PASSWORD}"
            ]) {
              sh './gradlew clean build -x test'
            }
            def grd_version = sh (
              script: "cat build.gradle.kts | grep -o 'version = [^,]*' | awk '{print \$3}'",
              returnStdout: true
            ).trim()
            print grd_version
            env.version = grd_version
          }
        }
      }
    }
    stage('Docker image Build and push') {
      steps {
        echo 'Docker step!'
        container('kaniko') {
          sh "executor --dockerfile=Dockerfile \
          --context=dir://${env.WORKSPACE} \
          --destination=${DOCKER_REGISTRY_URL}/ailis/tst-service:${ACTIVE_PROFILE}-${env.version}-${BUILD_NUMBER}"
        }
      }
    }
    stage('deploy') {
      steps {
        script {
            def remote = [:]
            remote.name = "${DEPLOY_REMOTE_NAME}"
            remote.host = "${DEPLOY_REMOTE_HOST}"
            remote.port = 22
            remote.allowAnyHosts = true

            def sshId = "${DEPLOY_CREDENTIALS_ID}"
            withCredentials([sshUserPrivateKey(credentialsId: sshId, keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'userName')]) {
                remote.user = userName
                remote.identityFile = identity
                stage("SSH Docker run") {
                    sshCommand remote: remote, command: "sed -e \"s/REPLACE_BUILD_VERSION_NUMBER/${ACTIVE_PROFILE}-${env.version}-${BUILD_NUMBER}/g\" -e \"s/REPLACE_TAG/${env.tag}/g\" -e \"s/REPLACE_PROFILE/${ACTIVE_PROFILE}/g\" ~/workspace/k8s-deploy/ailis/auto-deploy-temp/tst-deployment.template > ~/workspace/k8s-deploy/ailis/tst-deployment.yaml"
                    sshCommand remote: remote, command: "kubectl apply -f ~/workspace/k8s-deploy/ailis/tst-deployment.yaml"
                }
            }
        }
      }
    }
    stage('switching LB') {
      steps {
        script {
          def remote = [:]
          remote.name = "${DEPLOY_REMOTE_NAME}"
          remote.host = "${DEPLOY_REMOTE_HOST}"
          remote.port = 22
          remote.allowAnyHosts = true

          def sshId = "${DEPLOY_CREDENTIALS_ID}"
          withCredentials([sshUserPrivateKey(credentialsId: sshId, keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'userName')]) {
            remote.user = userName
            remote.identityFile = identity
            stage("SSH Docker run") {
                sshCommand remote: remote, command: "sh ~/workspace/k8s-deploy/ailis/lb/switchingLb.sh tst-dep ${env.tag} tst-service"
            }
          }
        }
      }
    }
  }
}
