CS5520 Final Project: Pet Adoption App

Table of Contents
Overview
Features
Technologies Used
Installation
Usage
Future Enhancements
Contributors
License

Overview
The Pet Adoption App is designed to assist users in finding their perfect pet. With functionalities like pet search, image-based pet identification, and an AI-powered chatbot, the app provides a seamless and engaging experience for users interested in pet adoption.

Features
Pet Search: Search for pets by type, breed, location, or age. View detailed pet profiles with adoption details.
Image-Based Pet Identification: Upload an image of a pet to identify its breed and other details, along with learning more about the pet.
Profile Management:
Access the AI Chatbot for interactive Q&A.
Sign Out functionality for easy account management.

Technologies Used
Development Environment: Android Studio
Programming Language: Java
Database: SQL
AI Services: Chatgpt
Authentication: Firebase Authentication
APIs: Chatgpt API

Installation
Prerequisites
Android Studio installed on your system.
Firebase project configured for authentication and database.
Steps
Clone the repository:
bash
git clone https://github.com/yanto1996/CS5520-Final-Project
cd CS5520-Final-Project
Open the project in Android Studio:
Select File > Open and navigate to the project folder.
Sync Gradle files:
Android Studio will prompt you to sync Gradle. Click Sync Now to ensure all dependencies are installed.
Set up Firebase:
Add your google-services.json file to the app/ directory.
Configure environment variables:
Add your API keys in a secure way (e.g., local.properties or directly in your code for testing purposes).
Build and run the app:
Connect your Android device or use an emulator.
Click Run or use Shift + F10 to launch the app.

Usage
Pages Overview
1. Pet Search
   Search for pets by type, breed, location, or age.
   View detailed profiles of pets, including contact details for adoption inquiries.
2. Upload Image
   Upload an image of a pet to:
   Identify the pet's breed.
   Learn additional details about the pet using AI-driven insights.
3. Profile Page
   AI Chatbot: Ask questions about pet care, adoption, or other related topics.
   Sign Out: Log out of your account.

Future Enhancements
Add real-time chat functionality with pet shelters.
Enable geolocation for more accurate search results.
Implement saved searches and favorite pets functionality.
Expand AI capabilities to provide behavior analysis or health predictions.

Contributors
Yan To
Xiaolin Liu
Liuyi Yang
Jiachen Liang


License
This project is licensed under the MIT License. See the LICENSE file for details.

