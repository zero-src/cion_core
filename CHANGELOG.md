# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [0.1.0] - 2026-05-26

### Added

- **Shared mod entrypoint** - initializes the Cion Core common and client environments.
- **Identifier helper** - `CionCore.id(String)` creates namespaced identifiers for `cion_core`.
- **ConfigManager** - generic JSON config loader and saver for Fabric mods:
  - Creates a default config file when one does not exist.
  - Loads existing JSON config files from Fabric's config directory.
  - Supports a `postLoad` callback for filling nullable defaults before re-saving.
  - Saves configs with pretty-printed JSON.
- **Access widener registration** - declares `cion_core.accesswidener` for shared access changes.
- **Fabric metadata** - includes mod icon, common/client entrypoints, dependencies, and Java 25 requirement.

### Dependencies

- Requires Fabric Loader `>= 0.19.2`.
- Requires Fabric API on Minecraft 26.2.
- Requires Java 25.

