# Auto Driving Car Simulator

## Tech Stack

1. Kotlin 2.0
2. JDK 21

## Assumptions

1. No limit constraint was specified for the field size, thus max limit of Long data type is used.
2. No error handling was specified for negative field size. Console will continuously parse for input if invalid value
   is provided.
3. Car name is made mandatory.
4. Stop moving the car if the new coordinate detected is outside the specified field size (e.g. o < x < width, o < y <
   height).
