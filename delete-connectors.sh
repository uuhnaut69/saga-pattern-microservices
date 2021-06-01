#!/bin/sh
curl -X DELETE http://localhost:8083/connectors/order_outbox_connector
curl -X DELETE http://localhost:8083/connectors/customer_outbox_connector
curl -X DELETE http://localhost:8083/connectors/inventory_outbox_connector