# TypeChefExtractor

![Build Status](https://jenkins.sse.uni-hildesheim.de/buildStatus/icon?job=KernelHaven_TypeChefExtractor)

A code-model extractor for [KernelHaven](https://github.com/KernelHaven/KernelHaven).

This extractor uses [TypeChef](https://ckaestne.github.io/TypeChef/) to analyze C source code.

## Capabilities

This extractor extracts a variability-aware abstract syntax tree (AST) from C source code files (`*.c`) and considers preprocessor macros.

## Usage

Place [`TypeChefExtractor.jar`](https://jenkins.sse.uni-hildesheim.de/view/KernelHaven/job/KernelHaven_TypeChefExtractor/lastSuccessfulBuild/artifact/build/jar/TypeChefExtractor.jar) in the plugins folder of KernelHaven.

To use this extractor, set `code.extractor.class` to `net.ssehub.kernel_haven.typechef.TypechefExtractor` in the KernelHaven properties.

## Dependencies

In addition to KernelHaven, this plugin has the following dependencies:
* [CnfUtils](https://github.com/KernelHaven/CnfUtils)

## License

This plugin is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html).
