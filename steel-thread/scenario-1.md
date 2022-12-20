# Steel Thread - 1

## Installing Tanzu PostgreSQL Operator for Steel Thread

Add the Tanzu Data Service package:

```
tanzu package repository add tanzu-data-services-repository \
    --url registry.tanzu.vmware.com/packages-for-vmware-tanzu-data-services/tds-packages:1.5.0 \
    --namespace tap-install
```

Check the version that is installed and configuration properties avaliable:

```
tanzu package available get postgres-operator.sql.tanzu.vmware.com/1.9.0 \
    --values-schema \
    --namespace tap-install
```

We only need to set the `dockerRegistrySecretName`.
Create a file named `postgres-operator-values.yaml` with the following content:

```
dockerRegistrySecretName: tap-registry
```

Finally, install the operator:

```
tanzu package install postgres-operator \
    --package-name postgres-operator.sql.tanzu.vmware.com \
    --version 1.9.0 \
    --namespace tap-install \
    -f postgres-operator-values.yaml
```

## Create the PostgreSQL database to use for customer-profile

Create a `service-instances` namespace:

```
kubectl create namespace service-instances
```

Add the image pull secret to the new namespace:

```
cat <<EOF | kubectl -n service-instances create -f -
apiVersion: v1
kind: Secret
metadata:
  name: tap-registry
  annotations:
    secretgen.carvel.dev/image-pull-secret: ""
type: kubernetes.io/dockerconfigjson
data:
  .dockerconfigjson: e30K
EOF
```

Create the service operator resources. From the root directory of this sample app:

```
kubectl apply -n service-instances -f ./config/service-operator
```

Wait for the database to become "Running"

```
kubectl get -n service-instances postgres/customer-database
```

Define the namespace where app will run:

```
export APP_NAMESPACE=my-apps
```

Create the app operator resources:

```
kubectl apply -n service-instances -f ./config/app-operator/postgres-resource-claim-policy.yaml
kubectl apply -n $APP_NAMESPACE -f ./config/app-operator/postgres-resource-claim.yaml
```

## Configure the Tekton testing pipeline

Run this command to create a `test-mvn` pipeline:

```
kubectl -n $APP_NAMESPACE apply -f config/test-mvn-pipeline.yaml
```

## Deploy the customer-profile app

Create the workload for the customer-profile app with external route:

```
tanzu apps workload create customer-profile -n $APP_NAMESPACE -f config/workload.yaml
```

For production deployment, create the workload for the customer-profile app with internal only route:

```
tanzu apps workload create customer-profile -n $APP_NAMESPACE -f config/workload.yaml --label "networking.knative.dev/visibility=cluster-local"
```
