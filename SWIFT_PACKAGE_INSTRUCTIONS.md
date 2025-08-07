# Swift Package Manager Integration Instructions

## Changes Made

The `createSwiftPackage` task in `library/build.gradle.kts` has been updated to:

1. Create a zip file of the XCFramework
2. Use a URL and checksum in the `Package.swift` file instead of a local path
3. Generate instructions for updating the checksum

## How to Use in Real-World Projects

### Building and Publishing the Framework

1. Build the XCFramework:
   ```
   ./gradlew assembleNetworkingXCFramework
   ```

2. Create the Swift Package:
   ```
   ./gradlew createSwiftPackage
   ```

3. Calculate the actual SHA-256 checksum of the zip file:
   - On macOS/Linux: `shasum -a 256 swift-package/networking.xcframework.zip`
   - On Windows: `certutil -hashfile swift-package/networking.xcframework.zip SHA256`

4. Update the checksum in `swift-package/Package.swift` with the actual value

5. Create a new release on GitHub:
   - Tag the release with the version number (e.g., `v1.0.0`)
   - Upload the `networking.xcframework.zip` file to the release

### Consuming the Framework in Swift Projects

Swift developers can add the package to their project using Swift Package Manager:

1. In Xcode, go to File > Swift Packages > Add Package Dependency
2. Enter the GitHub repository URL: `https://github.com/kotlin/allfunds-networking.git`
3. Specify the version requirements

### Updating the Framework

When updating the framework:

1. Update the version number in `library/build.gradle.kts`
2. Build the new XCFramework and create the Swift Package
3. Calculate the new checksum and update it in `Package.swift`
4. Create a new release on GitHub with the updated version tag
5. Upload the new zip file to the release

## Important Notes

- The URL in `Package.swift` must point to a publicly accessible location where the framework zip file can be downloaded
- The checksum must match the SHA-256 hash of the zip file exactly
- The placeholder checksum (all zeros) must be replaced with the actual value before the Swift package can be used

For more detailed instructions, refer to the `UPDATE_CHECKSUM.txt` file in the `swift-package` directory.