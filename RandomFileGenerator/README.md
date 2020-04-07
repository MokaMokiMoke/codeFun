# RandomFileGenerator

## What is this about?
A veeeeeery simple Java program to generate quick &amp; dirty random test files

## Does it support different dimensions?
Yes it does. It supports inputs as KB, MB, GB and TB (own enum class)

## How random is the random?
Not really random. The newly created files are simply filled with NULL Statements due to the creating Java funciton call. As long as the file sizes are differen (which should be different enuff) the checksums are different (by more than 99,99% of my tested files).
If you want someting Random... use something else.

## Limitations
- Far from "really" random
- Max. 2 GiB per File

## tl;dr
java --> rand files
