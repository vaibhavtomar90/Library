#!/bin/bash
export PACKAGE="fk-pricing-kaizen"
export PID_FILE=/var/run/${PACKAGE}.pid
export LOG_DIR=/var/log/flipkart/pricing/${PACKAGE}
export PRICING_LOG=${LOG_DIR}/${PACKAGE}.log
export PRICING_BASE=/var/lib/$PACKAGE
export ERROR_FILE=$LOG_DIR/$PACKAGE.err
export OUT_FILE=$LOG_DIR/$PACKAGE.out