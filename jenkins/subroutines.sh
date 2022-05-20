#!/bin/bash -

APP=`basename "$0" .sh`

#########################################################################
# Define functions

error() {
    TIMESTAMP="`date "+%F %T"`"
    echo "$TIMESTAMP ERROR [$APP] $@" >&2
}

warn() {
    TIMESTAMP="`date "+%F %T"`"
    echo "$TIMESTAMP WARN [$APP] $@" >&2
}

info() {
    TIMESTAMP="`date "+%F %T"`"
    echo "$TIMESTAMP INFO [$APP] $@"
}

# Predefined vars
declare -A APP_REPO_MAPPING=(
    ["public"]="https://github.com/cyppe/Trademax-Public"
    ["backoffice"]="https://github.com/cyppe/Trademax-Backoffice"
    ["legacy"]="https://github.com/cyppe/Trademax_Ecommerce"
    ["claims"]="https://github.com/cyppe/Trademax-Claims"
    ["public-shared-jobs"]="https://github.com/cyppe/Trademax-Public"
    ["images"]="https://github.com/cyppe/Trademax-Images"
    ["pim"]="https://github.com/cyppe/Trademax-PIM"
)

declare -A SITE_NAMESPACE_URL_MAPPING=(
  ["chilli-no"]="www.chilli.no"
  ["chilli-se"]="www.chilli.se"
  ["furniturebox-fi"]="www.furniturebox.fi"
  ["furniturebox-se"]="www.furniturebox.se"
  ["furniturebox-no"]="www.furniturebox.no"
  ["kodin1-com"]="www.kodin1.com"
  ["trademax-dk"]="www.trademax.dk"
  ["trademax-fi"]="www.trademax.fi"
  ["trademax-no"]="www.trademax.no"
  ["trademax-se"]="www.trademax.se"
  ["wegot-se"]="www.wegot.se"
)
declare -A SITE_CHANNEL_URL_MAPPING=(
  ["CHILLI_WEB_NO"]="www.chilli.no"
  ["CHILLI_WEB_SE"]="www.chilli.se"
  ["FB_WEB_FI"]="www.furniturebox.fi"
  ["FB_WEB_SE"]="www.furniturebox.se"
  ["FB_WEB_NO"]="www.furniturebox.no"
  ["KODIN1_WEB_FI"]="www.kodin1.com"
  ["WEB_DK"]="www.trademax.dk"
  ["WEB_FI"]="www.trademax.fi"
  ["WEB_NO"]="www.trademax.no"
  ["WEB_SE"]="www.trademax.se"
  ["WEGOT_WEB_SE"]="www.wegot.se"
)
declare -A SITE_CHANNEL_NAMESPACE_MAPPING=(
  ["CHILLI_WEB_NO"]="chilli-no"
  ["CHILLI_WEB_SE"]="chilli-se"
  ["FB_WEB_FI"]="furniturebox-fi"
  ["FB_WEB_SE"]="furniturebox-se"
  ["FB_WEB_NO"]="furniturebox-no"
  ["KODIN1_WEB_FI"]="kodin1.com"
  ["WEB_DK"]="trademax-dk"
  ["WEB_FI"]="trademax-fi"
  ["WEB_NO"]="trademax-no"
  ["WEB_SE"]="trademax-se"
  ["WEGOT_WEB_SE"]="wegot-se"
)
# Get active and inactive colors of public sites based on url
get_site_active_color() {
  local DOMAIN="${1}"
  local ACTIVE_COLOR=""
  ACTIVE_COLOR=$(curl --silent --location --max-time 10 "${DOMAIN}/get-color")
  if [[ "${ACTIVE_COLOR}" != "green" && "${ACTIVE_COLOR}" != "blue" ]] ; then
    echo "Failure getting current color"
    return 1
  fi
  echo "${ACTIVE_COLOR}"
}
get_site_inactive_color() {
  local DOMAIN="${1}"
  local ACTIVE_COLOR=""
  ACTIVE_COLOR=$(get_site_active_color "${DOMAIN}")
  case "$ACTIVE_COLOR" in
    "green") echo "blue"
      ;;
    "blue") echo "green"
      ;;
    *)
      echo "${ACTIVE_COLOR}"
      return 1
      ;;
  esac
}

get_slack_username() {
# READ THIS!!!
# We need to map Jenkins usernames to Slack usernames manually.
# Use Jenkins job error to get the username in Jenkins ("Failure getting Slack contact for username BlaBla")
# For Slack, use https://api.slack.com/methods/auth.test/test , login to Slack Trademax space.
# Click "Test Method", use "user" value from the output and add the mapping below.
# ["Jenkins_username"]="SlackUser"
  declare -A SLACK_USERS_MAPPING=(
      ["Alex Klomin"]="alex"
      ["Andriy Nazaruk"]="anazaruk2907"
      ["Borys Anikiyenko"]="borys.anikiyenko"
      ["Christoffer"]="christoffer.lundberg"
      ["Dmytro Feshchenko"]="dmytro.feschenko"
      ["Galyna Voitovych"]="galyna.voitovych"
      ["Halina Yatseniuk"]="galyna.yatseniuk"
      ["halynaminurka"]="halyna.minurka"
      ["Ihor Kordiak"]="ihor.kordiak"
      ["Kostya Zhevlakov"]="kostyantyn.zhevlakov"
      ["Liubomyr Chatoryiskyi"]="liubomyr"
      ["Paul Kmit"]="pavlo.kmit"
      ["Taras Yatsurak"]="taras.yatsurak"
      ["Volodymyr Hrushovenko"]="vova.hrushovenko"
      ["Volodymyr Rudak"]="krasavcheg"
      ["Yuliia Dytyniak"]="yuliia.dytyniak"
      ["maksforbytes"]="maksbahriy"
      ["Roman Babiak"]="roman.babiak"
      ["Nazar Demkovych"]="demko_n"
      ["Roman Kapatsila"]="roman.kapatsila"
      ["Yuriy Storozh"]="Yuriy.Storozh"
  )
  local JENKINS_USERNAME="$1"
  local SLACK_USERNAME=${SLACK_USERS_MAPPING["${JENKINS_USERNAME}"]}
  echo "${SLACK_USERNAME}"
}

get_image_commit_tag() {
  # Usage: get_image_commit_tag public latest
  # This will get commit tag (first in the list of tags) that relates to this "latest" tag
  # used to find out static commit-hash based tag from unstable branch tags
  local IMAGE_REGISTRY="eu.gcr.io/trademax-prod-186807"
  local APP=${1}
  local TAG=${2}
  local IMAGE_NAME=""
  case $APP in
    public)
      IMAGE_NAME=${IMAGE_REGISTRY}/publicsite ;;
    legacy)
      IMAGE_NAME=${IMAGE_REGISTRY}/legacysite ;;
    claims)
      IMAGE_NAME=${IMAGE_REGISTRY}/claimsite ;;
    *)
      IMAGE_NAME=${IMAGE_REGISTRY}/${APP} ;;
  esac
  local COMMIT_TAG=$(gcloud container images list-tags "${IMAGE_NAME}" --filter="tags:${TAG}" --limit=1 --format="value(tags[0])")
  echo "${COMMIT_TAG}"
}

# Retry once
function retry_once() {
  local COMMAND="$@"
  if ${COMMAND};then
      true
  else
      echo "ERROR: Failure running command, sleeping for 10s and retrying once more"
      sleep 10s
      ${COMMAND} || {
          echo "FATAL: Can't retry command, failing"
          exit 1
      }
  fi
}
# Retry once and if still failing - delete release completely and start over
# Note: this removes all resources in release, i.e. is risky if deploying not from scratch
function helm_retry_with_recreate() {
  local RELEASE="$1"
  local COMMAND="${@:2}"
  if ${COMMAND};then
      true
  else
      echo "ERROR: Failure running command, sleeping for 10s and retrying once more"
      sleep 10s
      if ${COMMAND} ;then
          echo "INFO: Successful retry"
      else
          echo "ERROR: Can't retry command, retrying with recreate"
          echo "WARN: The release will be completely purged (including created resources) and created again"
          helm delete "${RELEASE}" --purge
          sleep 5
          ${COMMAND} || {
              echo "FATAL: Can't install release, failing"
              exit 1
          }
      fi
  fi
}

function slack_notify_deployments() {
    # Sends message to slack #deployments channel
    local MESSAGE="${1}"
    local NAMESPACE="${2}"
    local BUILD_LINK="<${BUILD_URL}|${BUILD_DISPLAY_NAME}>"
    local SEVERITY="${3}"

    case "$SEVERITY" in
    "danger")
        local SEVERITY_EMOJI=":sos:"
        ;;
    "warning")
        local SEVERITY_EMOJI=":warning:"
        ;;
    "good")
        local SEVERITY_EMOJI=":white_check_mark:"
        ;;
    *)
        local SEVERITY_EMOJI=""
        ;;
    esac

    curl --max-time 30 -X POST --data-urlencode "payload={\"channel\": \"#deployments\", \"username\": \"deployer\", \"icon_emoji\": \":kubernetes:\", \"text\": \"${BUILD_LINK}: ${SEVERITY_EMOJI} ${MESSAGE}\", \"attachments\": [{\"color\": \"${SEVERITY}\", \"fields\": [{\"title\": \"${NAMESPACE}\"}]}]}" https://hooks.slack.com/services/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
}

function newrelic_notify_deployments() {
    # Sends message to NewRelic Deployments
    local NAME="${1}"
    local REVISION="${2}"
    local APP_IDS=""
    # Associative array of app name: applications ids in NewRelic
    declare -A APP_MAPPING=(
        [ecommerce-chilli-no]="127942656 124953107"
        [ecommerce-chilli-se]="125078466 127943122"
        [ecommerce-furniturebox-fi]="125077844 127942541"
        [ecommerce-furniturebox-no]="125077551 127934292"
        [ecommerce-furniturebox-se]="125077405 127933629"
        [ecommerce-kodin1-com]="125077520 127933552"
        [ecommerce-trademax-dk]="125077474 127927335"
        [ecommerce-trademax-fi]="124923899 127927381"
        [ecommerce-trademax-no]="125077517 127932956"
        [ecommerce-trademax-se]="125077877 127933480"
        [ecommerce-wegot-se]="125077501 127923875"
        [dashboard]="124954018 27927891"
        [legacy]="124954315 127927631"
        [claims]="124954348 127938676"
        [public-shared-jobs]="127930910"
        [pim]="178731910 178734409"
    )
    APP_IDS="${APP_MAPPING[${NAME}]}"
    for APP_ID in ${APP_IDS}; do
        # BUILD_URL is inherited from Jenkins
        curl --max-time 30 -X POST "https://api.newrelic.com/v2/applications/${APP_ID}/deployments.json" \
             -H 'X-Api-Key:e0e40dd2103134911bfc9d3c62168e7001d5af47121fcf5' -i \
             -H 'Content-Type: application/json' \
             -d \
        "{
          \"deployment\": {
            \"revision\": \"${REVISION}\",
            \"description\": \"${BUILD_URL}\"
          }
        }"
    done
}

wait_for_job() {
  local JOB_NAME="$1"
  echo "INFO: Waiting for $JOB_NAME to complete successfully or fail."
  JOB_RESULT=$(until kubectl --namespace=${NAMESPACE} get jobs ${JOB_NAME} -o jsonpath={.status.conditions..type} | grep 'Failed\|Complete\|Error' ;
  do
    sleep 10 ;
  done)
  if [ $JOB_RESULT = 'Complete' ]; then
    info "$JOB_NAME completed successfully."
  else
    error "$JOB_NAME failed."
    return 1
  fi
}