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
            url: "https://github.com/ferdyrod/networking-sample/releases/download/0.0.01/networking-0.0.01.zip",
            checksum: "d7b34d43f03f5f020f427798d5c7415a2b92e126f4ffc56537ed211011449954"
        ),
    ]
)