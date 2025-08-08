// swift-tools-version:5.9
import PackageDescription

let package = Package(
    name: "networking",
    platforms: [
        .iOS(.v14)
    ],
    products: [
        .library(
            name: "networking",
            targets: ["networking"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "networking",
            url: "",
            checksum: ""
        ),
    ]
)