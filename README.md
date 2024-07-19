# Auto Driving Car Simulator

## Tech Stack

1. Kotlin 2.0
2. JDK 21

## Technical

## Assumptions

### Console
- No specified expected behaviour if invalid input is provided. Console will continuously parse for input if invalid
value is provided.

### Field

- No limit constraint was specified for the field size, thus max limit of `Long` data type is used.
- No error handling was specified for **negative** field size. Prevent creation of field if negative value is provided.

### Simulation

- Car name is made mandatory and unique (case-sensitive).
- Stop moving the car if the new coordinate detected is outside the specified field
  size `(e.g. o <= x <= width, o <= y <= height)`.
- Stop moving the car if collision at initial state is detected.
- If more than 1 collision occurs in the field (e.g. 3 cars are in a collision), use the latest step count to reflect on
  which step all the cars went into collision
