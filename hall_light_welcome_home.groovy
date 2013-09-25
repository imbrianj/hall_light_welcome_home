/**
 *  Hall Light: Welcome Home
 *
 *  Author: brian@bevey.org
 *  Date: 9/25/13
 *
 *  Turn on the hall light if someone comes home (presence) and the door opens.
 */

preferences {
  section("People to watch for?") {
    input "people", "capability.presenceSensor", multiple: true
  }

  section("Front Door?") {
    input "sensors", "capability.contactSensor", multiple: true
  }

  section("Hall Light?") {
		input "lights", "capability.switch", title: "Switch Turned On", multilple: true
  }

  section("Presence Delay (defaults to 30s)?") {
    input name: "delaySeconds", type: "number", title: "How Long?", required: false
  }
}

def installed() {
  init()
}

def updated() {
  unsubscribe()
  init()
}

def init() {
  state.lastClosed = now()
  subscribe(people, "presence.present", presence)
  subscribe(sensors, "contact.open", doorOpened)
}

def presence(evt) {
	state.lastPresence = now()
}

def doorOpened(evt) {
  def delay = delaySeconds ?: 30

  if(now() - (delay * 1000) < state.lastPresence) {
    lights?.on()
  }
}