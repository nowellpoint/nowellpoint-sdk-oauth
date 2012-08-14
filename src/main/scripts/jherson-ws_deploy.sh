#!/bin/ksh

DEPLOYMENT_DIR=$HOME/workspace/sfa-qb/deployments
TARGET_DIR=$HOME/workspace/sfa-qb/target

rm  $DEPLOYMENT_DIR/*
cp $TARGET_DIR/quotebuilder.war $DEPLOYMENT_DIR
touch $DEPLOYMENT_DIR/quotebuilder.war.dodeploy
rsync -az $DEPLOYMENT_DIR/quotebuilder.war.dodeploy $DEPLOYMENT_DIR/quotebuilder.war jherson-ws.devlab.phx1.redhat.com:$JBOSS_HOME/standalone/deployments
