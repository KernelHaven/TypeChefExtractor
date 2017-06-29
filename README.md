# TypeChefExtractor

A code-model extractor for [KernelHaven](https://github.com/KernelHaven/KernelHaven).
This extractor uses [TypeChef](https://ckaestne.github.io/TypeChef/) to analyze C source code.

## Capabilities

This extractor extracts a variability-aware abstract syntax tree (AST) from C code files (`*.c`) and considers preprocessor macros.

## Usage

To use this extractor, set `code.extractor.class` to `net.ssehub.kernel_haven.typechef.TypechefExtractorFactory` in the KernelHaven properties.

### Dependencies

In addition to KernelHaven, this extractor has the following dependencies:
* [CnfUtils](https://github.com/KernelHaven/CnfUtils)
* [Typechef-0.4.1.jar](https://github.com/KernelHaven/TypeChefExtractor/blob/master/lib/TypeChef-0.4.1.jar) in the plugins directory

### Configuration

In addition to the default ones, this extractor has the following configuration options in the KernelHaven properties:

| Key | Mandatory | Default | Example | Description |
|-----|-----------|---------|---------|-------------|
