# TerraCraft
A simple Minecraft/Terraria like.

Welcome ! Here is my last attempt to make a game engine myself. For the record, at the beginning I tried to make a 3D game but I actually miss a lot of knowledge in matrix transformation... Moreover, OpenGL wasn't an option for me because I wanted a result quickly.

So here we are, I partially programmed a game engine using the Graphics2D API from Java. I chose Java because I am transitioning from Python to C++/C#.

## Firstly, some pictures of the project :

This is how the world looks like.
![1](https://user-images.githubusercontent.com/41203452/129716990-51840429-fcf3-49b2-b2d9-f950b5bc8636.PNG)
You can see the inventory system open and the portable crafter.
![2](https://user-images.githubusercontent.com/41203452/129716995-ca11beec-1056-4735-9445-6dccbe2479a6.PNG)
In this picture you can see what looks like a chest.
![3](https://user-images.githubusercontent.com/41203452/129716998-c7a875ba-4d22-4a7c-9364-e888bebb70bc.PNG)
The diffusion of light.
![4](https://user-images.githubusercontent.com/41203452/129717005-36f9afe0-382f-41b4-bde2-bafe2be94dce.PNG)
![5](https://user-images.githubusercontent.com/41203452/129717013-9eb94840-651f-45e9-8f50-e90255e01b2c.PNG)
The generation of caves.
![6](https://user-images.githubusercontent.com/41203452/129717016-7253d178-4da0-4704-93b9-02443e48d48b.PNG)

## Technologies used
- Procedural generation : the ground level is generated by 1D Perlin noise, the caves are generated by a cellular automata algorithm. The ores apparitions are generated by randomly "spots ores areas". The trees are generated at random positions and the plants also.
- Collision detection : I use multiple collision detection algorithms : by rectangles or rectangle and point. For the player collision, I optimized the code for it to only check if the player is in collision with the blocks around the player (in a certain range).
- Position transformation : due to the fact that the player is in the center of the screen, to get the block position by a click or the player position I developed some functions that convert the player and mouse position in chunkID, x and y relative position.
- GUI : all GUIs in the project are managed by the slotManager who permits the transfer of slots... Each slot can be rendered himself.
- Drop : when the player breaks a block, the block is transformed into a virtual slot. The drop has the ability to merge itself with a drop of the same type.
- The physics system : I chose for the player physics the system of forces. Each update, the player receives a force (gravity) and if he is on the floor and he jumps, his vertical velocity will increase by a certain amount.
- The light diffusion system : I used a recursive function with a light index model to create a light effect. Each recursion the light index reduces by a certain amount until it's less than the targeted block.
- There are other features that you will see if you launch the project...If you want to see more about these features you can check my code or search it on the Internet !

/!\ Warning : This is not a fully functional game. The purpose of the project was only to train me in Java programming and game engine related functionalities.

Feel free to contact me at romain.cortale@gmail.com
