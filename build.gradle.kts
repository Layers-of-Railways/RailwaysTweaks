import net.fabricmc.loom.api.LoomGradleExtensionAPI

plugins {
    id("fabric-loom") version "1.7.+"
}

version = "mod_version"()
group = "maven_group"()
base.archivesName.set("archives_base_name"())

repositories {
    maven("https://maven.quiltmc.org/repository/release") // Quilt Mappings
    maven("https://maven.parchmentmc.org") // Parchment mappings
    maven("https://mvn.devos.one/snapshots/") // Create, Porting Lib, Forge Tags, Milk Lib, Registrate
    maven("https://mvn.devos.one/releases/") // Porting Lib Releases
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // Forge Config API Port
    maven("https://maven.jamieswhiteshirt.com/libs-release") // Reach Entity Attributes
    maven("https://jitpack.io/") // Fabric ASM
    maven("https://maven.tterrag.com/") // Flywheel
    maven("https://api.modrinth.com/maven")
    maven("https://maven.ithundxr.dev/releases")
    maven("https://maven.shedaniel.me/") // Mantle deps
    exclusiveMaven("https://maven.ladysnake.org/releases", "dev.onyxstudios.cardinal-components-api") // Cardinal Components
    exclusiveMaven("https://maven.wispforest.io", "me.alphamode")
    exclusiveMaven("https://maven.terraformersmc.com/releases/", "com.terraformersmc")
    exclusiveMaven("https://repo.erdbeerbaerlp.de/repository/maven-public/", "de.erdbeerbaerlp")
    flatDir {
        dirs(setOf("libs"))
    }
}

dependencies {
    "minecraft"("com.mojang:minecraft:${"minecraft_version"()}")

    val loom = project.extensions.getByType<LoomGradleExtensionAPI>()
    mappings(loom.layered {
        officialMojangMappings { nameSyntheticMembers = false }
        parchment("org.parchmentmc.data:parchment-${"minecraft_version"()}:${"parchment_version"()}@zip")
    })

    modImplementation("net.fabricmc:fabric-loader:${"fabric_loader_version"()}")
    modImplementation("net.fabricmc.fabric-api:fabric-api:${"fabric_api_version"()}")

    // Hep
    modImplementation("maven.modrinth:hephaestus:${"minecraft_version"()}-${"hephaestus_version"()}")
    modImplementation("slimeknights.mantle:Mantle:${"minecraft_version"()}-${"mantle_version"()}")
    for (pl_module in "accessors,attributes,base,brewing,client_events,core,common,config,data,entity,extensions,items,models,model_loader,networking,tags,tool_actions,transfer,fluids,lazy_registration,loot,utility".split(",")) {
        modRuntimeOnly("io.github.fabricators_of_create.Porting-Lib:${pl_module}:2.3.4+1.20.1")
    }

    // Create
    modImplementation("com.simibubi.create:create-fabric-${"minecraft_version"()}:${"create_version"()}")

    // SNR & Numi
    modCompileOnly("com.railwayteam.railways:Steam_Rails-fabric-1.20.1:1.6.4+fabric-mc1.20.1")
    modCompileOnly("dev.ithundxr.createnumismatics:CreateNumismatics-fabric-1.20.1:1.0.6+fabric-mc1.20.1")

    // Workarounds
    modCompileOnly("maven.modrinth:copycats:fabric.1.20.1-1.3.2")
    modCompileOnly("maven.modrinth:appleskin:2.5.1+mc1.20")
    
    // Compat
    modImplementation("maven.modrinth:banhammer:0.7.1+1.20.1")
    modImplementation(":ACME_Admin-0.1.0-beta.1+fabric-mc1.20.1-local")

    modImplementation("de.erdbeerbaerlp:dcintegration.common:3.0.7") {
        isChanging = true
        isTransitive = false
    }
}

tasks.processResources {
    // set up properties for filling into metadata
    val properties = mapOf(
            "version" to version,
            "fabric_loader_version" to "fabric_loader_version"(),
            "fabric_api_version" to "fabric_api_version"(),
            "minecraft_version" to "minecraft_version"(),
    )

    inputs.properties(properties)

    filesMatching("fabric.mod.json") {
        expand(properties)
    }
}

val targetJavaVersion = 17
tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible()) {
        options.release.set(targetJavaVersion)
    }
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion = JavaLanguageVersion.of(targetJavaVersion)
    }
    withSourcesJar()
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName}"}
    }
}

operator fun String.invoke(): String {
    return rootProject.ext[this] as? String
        ?: throw IllegalStateException("Property $this is not defined")
}

fun RepositoryHandler.exclusiveMaven(url: String, vararg groups: String) {
    exclusiveContent {
        forRepository { maven(url) }
        filter {
            groups.forEach {
                includeGroup(it)
            }
        }
    }
}