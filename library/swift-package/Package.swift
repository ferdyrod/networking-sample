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
            path: "networking.xcframework"
        ),
    ]
)