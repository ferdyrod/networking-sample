// swift-tools-version:5.3
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
            url: "https://github.com/kotlin/allfunds-networking/releases/download/v1.0.0/networking.xcframework.zip",
            checksum: "0000000000000000000000000000000000000000000000000000000000000000"
        ),
    ]
)