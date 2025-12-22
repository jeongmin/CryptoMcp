package org.laggyrocket


import io.modelcontextprotocol.kotlin.sdk.server.Server
import io.modelcontextprotocol.kotlin.sdk.server.ServerOptions
import io.modelcontextprotocol.kotlin.sdk.server.StdioServerTransport
import io.modelcontextprotocol.kotlin.sdk.types.CallToolResult
import io.modelcontextprotocol.kotlin.sdk.types.Implementation
import io.modelcontextprotocol.kotlin.sdk.types.ServerCapabilities
import io.modelcontextprotocol.kotlin.sdk.types.TextContent
import kotlinx.serialization.json.*
import kotlinx.io.asSource
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.coroutines.*

fun main() = runBlocking {
    // Create MCP server instance
    val server = Server(
        serverInfo = Implementation(
            name = "crypto-mcp-server",
            version = "0.0.1"
        ),
        options = ServerOptions(
            capabilities = ServerCapabilities(
                tools = ServerCapabilities.Tools(listChanged = true)
            )
        )
    ) {
        "A basic MCP server for cryptocurrency information"
    }

    // Add a simple echo tool
    server.addTool(
        name = "echo",
        description = "Echoes back the provided message"
    ) { request ->
        val message = request.params.arguments?.get("message")?.jsonPrimitive?.content ?: "No message provided"

        CallToolResult(
            content = listOf(
                TextContent(
                    text = "Echo: $message"
                )
            )
        )
    }

    // Add a greeting tool
    server.addTool(
        name = "greet",
        description = "Greets a person by name"
    ) { request ->
        val name = request.params.arguments?.get("name")?.jsonPrimitive?.content ?: "stranger"

        CallToolResult(
            content = listOf(
                TextContent(
                    text = "Hello, $name! Welcome to the CryptoMcp server."
                )
            )
        )
    }

    // Add a simple crypto info tool (placeholder)
    server.addTool(
        name = "get_crypto_info",
        description = "Get basic information about a cryptocurrency (placeholder)"
    ) { request ->
        val symbol = request.params.arguments?.get("symbol")?.jsonPrimitive?.content ?: "BTC"

        CallToolResult(
            content = listOf(
                TextContent(
                    text = "Cryptocurrency: $symbol\nThis is a placeholder. Real crypto data integration coming soon!"
                )
            )
        )
    }

    println("Starting MCP Server: crypto-mcp-server v1.0.0")
    println("Available tools: echo, greet, get_crypto_info")
    println("Listening on STDIO...")

    // Connect to STDIO transport and start the server
    val transport = StdioServerTransport(
        inputStream = System.`in`.asSource().buffered(),
        outputStream = System.out.asSink().buffered()
    )

    server.createSession(transport)
    val done = Job()
    server.onClose {
        done.complete()
    }
    done.join()
}