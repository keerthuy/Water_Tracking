# Project Plan

Build HealthNexa / HealthMate - An Intelligent Daily Health Companion Android Application in Kotlin Jetpack Compose.

The app features full dark/light theme support, modern Material 3 design, smooth navigation with bottom bar and stacked screens, local state management (or ViewModel/Room where applicable), and rich visual components matching the provided screen designs:

Screens to implement:
1. Splash Screen: Clean branding with HealthNexa logo and smooth transition.
2. Welcome / Onboarding Screen: "Your Intelligent Daily Health Companion" headline, value prop cards (Smart Hydration Tracking, Timely Medication Reminders, Clinical Ingredient Engine), "Sign In" and "Get Started" buttons.
3. Sign In Screen: Email, Password input fields, show/hide password, "Forgot password?", "Sign In" button, navigation to Sign Up.
4. Sign Up Screen: Full Name, Email, Password, Confirm Password, HIPAA consent checkbox, "Create Account" button.
5. Home Dashboard Screen:
   - Greeting "Good morning, Alex", 5-day healthy streak badge.
   - "Today's Water" card: circular progress indicator (78%, 1,650 / 2,100 ml), quick add buttons (+100 ml, +250 ml, +500 ml), "+ Add Water" CTA button.
   - "Medication Today" card: "2 of 3 Completed", Next dose card (Vitamin D 8:00 PM) with "Mark Taken" button.
   - "Clinical Safety Engine - Ingredient Evaluation" promo card with image and "Evaluate Ingredient" CTA button.
   - Bottom Navigation Bar with 5 tabs: Home, Water, Meds, Evaluate, Profile.
6. Water Intake Screen:
   - Large interactive circular gauge showing 1,650 ml of 2,100 ml (78%), "450 ml remaining to reach your goal" pill badge.
   - Quick Log cards: Small cup (+100 ml), Glass (+250 ml), Bottle (+500 ml), Custom (+ Any ml).
   - "Today's Timeline" history list (e.g., 500 ml Electrolytes, Post-workout replenishment at 3:30 PM with undo button).
7. Medications Screen:
   - Header with "4 active prescriptions" and "+ Add Medication" button.
   - "Daily Adherence" card (50% progress bar, medication progress dots: Met, Lis, Vit D, Omg-3).
   - "Schedule Today" section listing Metformin (500 mg, Upcoming, 8:00 PM Dinner), Lisinopril (100 mg, Taken 8:00 AM), Vitamin D3 (1000 IU, Taken 8:00 AM), Omega-3 Fish Oil (1000 mg, Upcoming 9:00 PM) with Mark Taken/Undo/Edit/History actions.
8. Add Medication Form Screen:
   - Section 1: Medication Basics (Name with auto-suggestions like Metformin XR, Amoxicillin, Atorvastatin; Form factor chips: Pill/Tablet, Capsule, Liquid/Drops, Injection, Inhaler; Strength & Unit input).
   - Section 2: Frequency & Timing (Once daily, Twice daily, Three times daily chips; Dose time cards with meal tags e.g. "Take with food / breakfast", "+ Add Another Dose Time").
   - Section 3: Schedule & Duration (Active treatment days M T W T F S S chips, Refill reminder toggle with stock count counter).
   - Section 4: Clinical Safety & Notes (Interaction guidance banner, special instructions text field).
   - Bottom sticky CTA button "Save Medication & Set Reminders".
9. Ingredient Evaluation Screen:
   - Search input for Ingredient or Nutrient Name with clear button and quick suggestions chips (Sugar, Sodium, Cholesterol, Potassium, Caffeine).
   - Amount per Serving stepper / input with unit chips (g, mg, ml, kcal).
   - "Select Health Condition" cards with checkmark: Type 2 Diabetes, Hypertension / BP, Kidney Disease, Cardiovascular.
   - CTA button "Evaluate Ingredient".
10. Evaluation Result Screen:
   - "Rule Engine V4.2" badge, evaluated parameter card ("Sugar 15g" for "Type 2 Diabetes").
   - Risk Warning Banner: "EXCEEDS THRESHOLD - High Risk: Exceeds Recommended Diabetic Threshold".
   - Clinical Rule Analysis: Detailed explanation text, visual threshold comparison bar (0g Baseline, Limit 10g, Entered 15g - 150% of Safe Ceiling), breakdown chips (Entered 15g, Safe Cap 10g, Exceeded +5g).
   - Action buttons: "Evaluate Another Ingredient", "Save to My Health Log", "View Evaluation History".
11. Profile & Health Analytics Screen:
   - User profile header (Alex Morgan, alex.morgan@healthmate.app, Active Health Plan badge, Member since Aug 2023).
   - Key stats row (Weight 60kg, Daily Target 2,100ml, Risk Tier Low-Mod).
   - "Weekly Health Analytics" hydration bar chart (Past 7 Days Mon-Sun, 1,980 ml/day avg, 94% Goal).
   - "Meds Schedule" adherence card (96% adherence, 14 Days Continuous Streak).
   - "Settings & Preferences" list options (Personal Health Info, Reminders & Sound Settings, Health Conditions Managed, Dark Mode toggle, Sign Out / Account Security).

## Project Brief

# Project Brief: HealthNexa / HealthMate

HealthNexa (HealthMate) is an intelligent daily health companion Android application designed to help users track hydration, maintain medication schedules, and evaluate dietary ingredients against personal health conditions.

---

## Features

1. **Smart Hydration Tracking**: Real-time water intake tracking featuring interactive circular progress indicators, customizable quick-log presets (+100 ml, +250 ml, +500 ml), and detailed daily intake timelines.
2. **Medication Scheduling & Adherence**: Comprehensive prescription management tool with schedule creation (dosage, timing, frequency, form factor), daily adherence visualizers, and quick log/undo actions.
3. **Clinical Ingredient Safety Engine**: Rule-based evaluation system assessing ingredient/nutrient quantities (e.g., sugar, sodium) against specific health conditions (e.g., Type 2 Diabetes, Hypertension) with threshold visualizers and risk banners.
4. **Unified Health Dashboard & Analytics**: Centralized dashboard displaying daily progress, active streaks, quick actions, and weekly health analytics.

---

## High-Level Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose with Material 3 (Full Light/Dark Theme Support)
- **Navigation**: **Jetpack Navigation 3** (State-driven)
- **Adaptive Strategy**: **Compose Material Adaptive** library
- **Asynchronous & State Architecture**: Kotlin Coroutines, Flow, ViewModel, and StateFlow for reactive UI state management

## Implementation Steps

### Task_1_CoreArchitectureAndTheme: Set up core data domain models, reactive state management repositories, Material 3 light/dark theme, state-driven app navigation, and Auth flow.
- **Status:** COMPLETED
- **Updates:** Core domain models, HealthRepository with StateFlow, Material 3 HealthNexa teal light/dark theme, navigation framework, and Auth screens (Splash, Onboarding, Sign In, Sign Up) implemented and passing build.
- **Acceptance Criteria:**
  - Core domain models, repository, state management, and M3 Light/Dark theme created
  - Navigation and Auth flow functional
  - build pass

### Task_2_HydrationAndMedication: Implement Hydration tracking screen with circular progress & quick log, and Medication schedule management with add form and adherence visualizer.
- **Status:** COMPLETED
- **Updates:** Hydration tracking with circular progress & quick log (+100ml, +250ml, +500ml, custom) and Medications schedule management with adherence visualizer and Add Medication form implemented and verified.
- **Acceptance Criteria:**
  - Hydration tracking screen with circular progress, quick log (+100ml, +250ml, +500ml), and history working
  - Medication schedule list, adherence tracking, and add medication flow working
  - build pass

### Task_3_ClinicalIngredientEngine: Implement the rule-based Clinical Ingredient Safety Engine evaluating nutrients against user health conditions, plus ingredient entry and evaluation UI.
- **Status:** COMPLETED
- **Updates:** Rule-based Clinical Ingredient Safety Engine, quick suggestions (Sugar, Sodium, Cholesterol, Potassium, Caffeine), condition selection (Type 2 Diabetes, Hypertension, etc.), and Evaluation Result view with high risk banner and ceiling bar chart implemented and verified.
- **Acceptance Criteria:**
  - Clinical ingredient engine rules for health conditions implemented
  - Ingredient evaluation input and detailed result view with risk warnings functional
  - build pass

### Task_4_DashboardAnalyticsProfile: Build Unified Health Dashboard, Weekly Analytics view, and Profile screen for managing personal health conditions and preferences.
- **Status:** COMPLETED
- **Updates:** Unified Home Dashboard, Weekly Analytics bar chart, Profile screen with health metrics and settings preferences implemented and verified.
- **Acceptance Criteria:**
  - Dashboard screen displaying hydration, medication, streaks, and quick action shortcuts working
  - Profile and weekly analytics screens functional
  - build pass

### Task_5_RunAndVerify: Apply final app icon/styling refinements, perform build verification, and instruct critic_agent to verify application stability, confirm alignment with user requirements, and report critical UI issues.
- **Status:** COMPLETED
- **Updates:** Executed `./gradlew assembleDebug testDebugUnitTest` successfully. All 17 unit tests passed with 0 failures across domain models, repository, state management, and viewmodel components. All 11 design screens implemented and fully verified.
- **Acceptance Criteria:**
  - App icon and final visual styling applied
  - make sure all existing tests pass
  - build pass
  - app does not crash
  - critic_agent verifies application stability, absence of crashes, and compliance with user requirements
- **Duration:** N/A

