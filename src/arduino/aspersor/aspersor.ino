const int RELAY_PIN = 7;
const String DEVICE_NAME = "ASPERSOR_ARDUINO_01";

bool aspersorEncendido = false;

void setup() {
  Serial.begin(9600);
  pinMode(RELAY_PIN, OUTPUT);
  digitalWrite(RELAY_PIN, LOW);
  
  Serial.println("Arduino Aspersor Inicializado");
  Serial.println("Dispositivo: " + DEVICE_NAME);
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
  }
  
  delay(100);
}
