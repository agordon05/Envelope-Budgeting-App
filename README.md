# Envelope Budgeting App Prototype

# Description
I built this budgeting app to not only make budgeting easier for myself, but to also to take my code to a more professional level. This project was made to digitalize the envelope budgeting method in an easy to understand while keeping it functional.

One thing I learned from this project is that the double data type is not good for calculations and one of the ways to solve that was to use a class called BigDecimal.

What stands out in my project compared to other budgeting apps like mint is that my project gives the user complete control on how their budget is run. It also isn't cluttered with information that most users aren't interested in.

# Layout
At the top of the main window are three buttons: withdraw, deposit and transfer. these buttons will open a new window for the user to fill out necessary information to complete the specified task. Under those buttons is the balance. The balance is the total amount of every envelope. Under the balance are the envelopes. 
The information of each envelope is laid out in a specific order. that order is priority, name, amount, cap amount, fill setting, and then there is an edit button. The edit button allows the user to edit the envelope and/or remove it.
Under the envelopes is an add envelope button. This button allows the user to create a new envelope. This button will disapear when the user has 10 envelopes

# Note
Envelopes cannot have the same name.
If a window doesn't close when clicking the submit button. Either information is missing or their is a conflict with the information given.
If it looks like information is missing/hasn't been updated, try resizing the window.

# How to run
This project is run using JavaSE-15, Swing and AWT. The main method is inside the PrototypeUI class in the UI package. This project has an Executable Jar file called EbaPrototype.jar. This project will run automatically by opening that file. However, by using that jar file, any changes you make will **not** be saved for later use.
