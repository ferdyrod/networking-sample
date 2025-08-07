// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "networking",
    platforms: [
        .iOS(.v16)
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