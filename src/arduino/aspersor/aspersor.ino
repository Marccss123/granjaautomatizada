const int RELAY_PIN = 7;
const int LED_PIN = 13;
const String DEVICE_NAME = "ASPERSOR_ARDUINO_01";

bool aspersorEncendido = false;
bool aspersorConectado = false;

void setup() {
  Serial.begin(9600);
  pinMode(RELAY_PIN, OUTPUT);
  pinMode(LED_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, LOW);
  digitalWrite(LED_PIN, LOW);
  
  Serial.println("Arduino Aspersor Inicializado");
  Serial.println("Dispositivo: " + DEVICE_NAME);
  Serial.println("LED en pin 13 - Apagado por defecto");
}

void loop() {
  if (Serial.available() > 0) {
    String comando = Serial.readStringUntil('\n');
    comando.trim();
    
    if (comando == "GET_NAME") {
      Serial.println(DEVICE_NAME);
    }
    else if (comando == "ACTIVATE_SPRINKLER") {
      digitalWrite(RELAY_PIN, HIGH);
      aspersorEncendido = true;
      Serial.println("OK");
    }
    else if (comando == "DEACTIVATE_SPRINKLER") {
      digitalWrite(RELAY_PIN, LOW);
      aspersorEncendido = false;
      Serial.println("OK");
    }
    else if (comando == "GET_STATUS") {
      if (aspersorEncendido) {
        Serial.println("ON");
      } else {
        Serial.println("OFF");
      }
    }
    else if (comando == "CONNECT") {
      aspersorConectado = true;
      digitalWrite(LED_PIN, HIGH);
      Serial.println("CONNECTED");
    }
    else if (comando == "DISCONNECT") {
      aspersorConectado = false;
      digitalWrite(LED_PIN, LOW);
      Serial.println("DISCONNECTED");
    }
    else if (comando == "GET_CONNECTION_STATUS") {
      if (aspersorConectado) {
        Serial.println("CONNECTED");
      } else {
        Serial.println("DISCONNECTED");
      }
    }
  }
  
  delay(100);
}
