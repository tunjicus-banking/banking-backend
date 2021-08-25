#!/bin/bash
wait_time=15s
password=Strong_Pa55word
currentDate="$(date +%Y%m%d)"
export currentDate=$currentDate
echo "$currentDate"

# wait for SQL Server to come up
echo importing data will start in $wait_time...
sleep $wait_time
echo importing data...

function run_script {
  if [[ -z $1 ]]; then
    echo "missing argument"
  fi
  /opt/mssql-tools/bin/sqlcmd -S 0.0.0.0 -U sa -P $password -i "./$1.sql"
}

# Create db
run_script "create_db"

# Create tables
run_script "create_banks"
run_script "create_users"
run_script "create_accounts"
run_script "create_savings"
run_script "create_checking"
run_script "create_transactions"
run_script "create_companies"
run_script "create_positions"
run_script "create_job_postings"
run_script "create_offers"
run_script "create_employment_history"

# Insert data
run_script "insert_banks"
run_script "insert_users"
run_script "insert_accounts"
run_script "insert_savings"
run_script "insert_checking"
run_script "insert_companies"
run_script "insert_positions"
run_script "insert_job_postings"