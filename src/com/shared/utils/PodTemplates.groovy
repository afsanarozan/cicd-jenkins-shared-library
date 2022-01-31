package com.shared.utils

public void generalTemplate(body) {
  podTemplate(
        containers: [containerTemplate(name: 'docker', image: 'docker.io/docker', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'helm', image: 'docker.io/alpine/helm', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'curl', image: 'centos', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'k6', image: 'coralspec/k6', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'aws', image: 'amazon/aws-cli', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'kubectl', image: 'dtzar/helm-kubectl', command: 'cat', ttyEnabled: true),
        containerTemplate(name: 'golang', image: 'golang:1.15', command: 'cat', ttyEnabled: true),
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
