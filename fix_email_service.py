#!/usr/bin/env python3
"""Fix EmailServiceImpl.java - Change order.getStatus() to order.getStatus().toString()"""

file_path = r".\backend\eyevision\src\main\java\com\eyevision\eyevision\serviceimplement\EmailServiceImpl.java"

# Read the file
with open(file_path, 'r', encoding='utf-8') as f:
    content = f.read()

# Replace the problematic line
# Looking for: order.getStatus(),  (in the buildOrderStatusUpdateEmail method arguments)
# Should be: order.getStatus().toString(),

content = content.replace(
    "             order.getStatus(),\n             statusEmoji,",
    "             order.getStatus().toString(),\n             statusEmoji,"
)

# Write back to file
with open(file_path, 'w', encoding='utf-8') as f:
    f.write(content)

print("Fixed EmailServiceImpl.java - Changed order.getStatus() to order.getStatus().toString()")
