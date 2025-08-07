// swift-tools-version:5.3
import PackageDescription

let package = Package(
    name: "AllfundsNetworking",
    platforms: [
        .iOS(.v16)
    ],
    products: [
        .library(
            name: "AllfundsNetworking",
            targets: ["AllfundsNetworking"]
        ),
    ],
    targets: [
        .binaryTarget(
            name: "AllfundsNetworking",
            url: "https://github.com/ferdyrod/networking-sample/releases/download/0.0.01/networking-0.0.01.zip"
            path: "networking.xcframework"
        ),
    ]
)