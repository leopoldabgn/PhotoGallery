# PhotoGallery
Photo Gallery coded in java

## INSTALLATION
You can download the last release. There is a tar or zip file. untar/unzip the folder and execute the command :
java -jar photoGallery.jar

Otherwise you can download the source code and compile it yourself. This is a maven project. So you can run the following command to compile:
mvn package

Then the jar created is in the "./target" folder. It must be moved to the folder above which contains "paint.jar":
mv target/photoGallery.jar ../

Launch the photo gallery:
cd..
java -jar photoGallery.jar

## HOW TO USE
### Add a folder
First, you can add a folder by clicking the "Add Folder" button on the left side. Then, all you have to do is select the folder whose photos you want to see, then validate the dialog box.

!! __WARNING__ !!
If the folder doesn't contain a photo, then it will not appear. And you will not be able to perform any action on it.

Once you have selected your folder, you can click on it:
- If you click 1 time, then all the photos contained in the folder __AND__ in its sub-folders will appear.
- If you click twice, then only the photos contained in the folder will appear. Photos from its subfolders will __NOT__ appear.
![Capture d’écran du 2022-07-07 11-56-04](https://user-images.githubusercontent.com/95108507/177749386-6c4cf8fb-0336-49a6-a4c0-6681488399b0.png)
*Here you can see all the images contained in my github folder and its subfolders*

### Preset
All the settings and folders you have chosen are saved in a __preset.ini__ file. All your settings will be restored the next time you launch the app. You can choose to:
- Delete your preset by going to: File -> Reset Preset
- Create a new preset: File -> New Preset
- Open a preset located in another folder: File -> Open Preset

### Workspace
The workspace will allow you to make a selection of photos. All you have to do is drag and drop all images you want. Once moved into the workspace, they will appear inside. When you have completed a selection, you can:
- export all images to a folder. Simply click on Tools -> Export.... in the toolbar of your workspace.
- delete all images from your workspace by clicking on Tools -> clear.
![Capture d’écran du 2022-07-07 12-27-08](https://user-images.githubusercontent.com/95108507/177755046-9b274fd4-b881-451e-8024-d72325766750.png)
*Vous pouvez voir les images dans le workspace. Le bouton Tools également*
Otherwise, you can of course close it if you don't need it. To reopen it later, use Tools -> Open Workspace button in the window

### Toolbar
- Edit button: it's not working actually.
- Window -> Reset Perspective : You can reset all the perspective.
- File : Go to Preset explanation to get further informations.
- Tools -> Open Workspace : To open the workspace

### Images
You can select an image by clicking once on it then use the arrows to go from left to right. If you double click on an image, you will enter the PictureViewer. (Or press ENTER).  
You can also right click on the image to:
- View properties
- Move/Copy the image to another folder

### PictureViewer
It allows you to observe images and modify them. You can :
- browse the images using the arrow keys
- zoom using the mouse, wheel or +/- button. You can also double click to zoom in/out.
- Cropping: You can crop the image by clicking on the button at the top left.
![Capture d’écran du 2022-07-07 12-51-05](https://user-images.githubusercontent.com/95108507/177757730-a607e152-c8c0-4c24-ab66-82ee1aa3ceb8.png)

- Filters: the funnel-shaped button allows you to apply several filters to your image.
![Capture d’écran du 2022-07-07 12-50-45](https://user-images.githubusercontent.com/95108507/177757688-f1d13e2a-9861-45f4-80f5-93ffbafff827.png)

- Paint: The pencil button will allow you to open your image in an image editor that I created (paint.jar). Once inside you can draw on the image or modify it. Then save it. I invite you to see the link to this project: **paint.jar**
![Capture d’écran du 2022-07-07 12-58-45](https://user-images.githubusercontent.com/95108507/177758156-51eac8cc-0b9b-44b2-be78-47a12b2fbfc1.png)

- Reset: the reset button will remove the effects you have applied to your image.
- Delete: The cross allows you to delete the image.
![Capture d’écran du 2022-07-07 12-55-23](https://user-images.githubusercontent.com/95108507/177758183-9ac798cd-4c4e-4ca6-8fa3-5a18567501e0.png)

### Keyboard shortcuts
- Suppr : Delete an image.
- Left/right arrow : browse the images.
- Escape : Quit the photoGallery or the PictureViewer.
- Enter : open an image in the PictureViewer.
