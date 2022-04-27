package com.shared.utils

public void generalTemplate(body) {
  podTemplate(
        containers: [containerTemplate(name: 'docker', image: 'docker.io/docker', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'helm', image: 'docker.io/alpine/helm', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'curl', image: 'centos', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'k6', image: 'coralspec/k6', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'sonarscanner', image: 'sonarscannerbuildkite/sonarscanner:0.1.1', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'aws', image: 'amazon/aws-cli', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'kubectl', image: 'dtzar/helm-kubectl', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'golang', image: 'golang:1.18.0', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'ubuntu', image: 'ubuntu', command: 'cat', ttyEnabled: true),
        containerTemplate(
          name: 's3cmd', 
          image: 'rbsdev/s3cmd',
          envVars: [ envVar(key: 'AWS_ACCESS_KEY_ID', value: 'YFRP3PS4LIJEOZVRUMMK'),
                     envVar(key: 'AWS_SECRET_ACCESS_KEY', value: '0s4FQ470cF9AGDg7old5fLyvvhbhnqO99ooruvQdVOs'),
                     envVar(key: 'AWS_DEFAULT_REGION', value: 'sgp1'),
                     envVar(key: 'host', value: 'sgp1.digitaloceanspaces.com'),
                     envVar(key: 'host-bucket', value: '%(bucket)s.sgp1.digitaloceanspaces.com')], 
          command: 'cat', 
          ttyEnabled: true),
        containerTemplate(
              name: 'aws-cli', 
              image: 'mesosphere/aws-cli', 
              envVars: [ envVar(key: 'AWS_ACCESS_KEY_ID', value: 'YFRP3PS4LIJEOZVRUMMK'),
                         envVar(key: 'AWS_SECRET_ACCESS_KEY', value: '0s4FQ470cF9AGDg7old5fLyvvhbhnqO99ooruvQdVOs'),
                         envVar(key: 'AWS_DEFAULT_REGION', value: 'sgp1')],
              command: 'cat', 
              ttyEnabled: true),
            containerTemplate(
              name: 'jfrog-go', 
              image: 'releases-docker.jfrog.io/jfrog/jfrog-cli-full-v2', 
              envVars: [ envVar(key: 'JFROG_CLI_LOG_LEVEL', value: 'DEBUG')],
              command: 'cat', 
              ttyEnabled: true),
        containerTemplate(name: 'base', image: 'dgrlabs/base-runner:latest', command: 'cat', ttyEnabled: true)],
        volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]) {
    body.call()
}
}

public void dockerTemplate(body) {
  podTemplate(
        containers: [containerTemplate(name: 'docker', image: 'docker', command: 'cat', ttyEnabled: true)],
        volumes: [hostPathVolume(hostPath: '/var/run/docker.sock', mountPath: '/var/run/docker.sock')]) {
    body.call()
}
}

public void mavenTemplate(body) {
  podTemplate(
        containers: [containerTemplate(name: 'maven', image: 'maven', command: 'cat', ttyEnabled: true)],
        volumes: [secretVolume(secretName: 'maven-settings', mountPath: '/root/.m2'),
                  persistentVolumeClaim(claimName: 'maven-local-repo', mountPath: '/root/.m2nrepo')]) {
    body.call()
}
}

return this
