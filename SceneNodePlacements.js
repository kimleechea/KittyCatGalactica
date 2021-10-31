var JavaPackages = new JavaImporter(
    Packages.ray.rage.scene.SceneManager,
    Packages.ray.rage.scene.controllers,
    Packages.ray.rage.scene.generic,
    Packages.ray.rage.scene.Light.Type.POINT,
    Packages.java.awt.Color
);

with (JavaPackages) {

    tessellationN.scale(parseFloat(400),parseFloat(400),parseFloat(400));
    tessellationN.moveDown(parseFloat(2.0));
	//tessellationN.moveBackward(parseFloat(0.0));
    
    tessellationE.setHeightMap(eng, "blue.jpeg");
	tessellationE.setTexture(eng, "arena.png");
    
    //dolphinN.moveDown(parseFloat(2.0));
    //dolphinN.moveUp(parseFloat(0.0));
    //dolphinN.moveForward(parseFloat(20.0));
    dolphinN.scale(parseFloat(0.1),parseFloat(0.1),parseFloat(0.1));
    dolphinN.rotate(rotationAngleAmt, dolphinN.getLocalUpAxis());
    //dolphinN.setLocalPosition(parseFloat(0), parseFloat(-1.73), parseFloat(0));

    station2N.moveForward(parseFloat(80.0));
    station2N.moveUp(parseFloat(10.0));
    station2N.lookAt(parseFloat(0.0), parseFloat(10.0), parseFloat(0.0));
    //station2N.lookAt(origin);

    station1N.moveBackward(parseFloat(80.0));
    station1N.moveUp(parseFloat(10.0));
    //station1N.rotate(rotationAngleAmt, dolphinN.getLocalUpAxis());
    station1N.lookAt(parseFloat(0.0), parseFloat(10.0), parseFloat(0.0));

    groundNode.setLocalPosition( parseFloat(0), parseFloat(0) , parseFloat(0) );
	
}