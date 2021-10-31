var JavaPackages = new JavaImporter(
    Packages.ray.rage.scene.SceneManager,
    Packages.ray.rage.scene.controllers,
    Packages.ray.rage.scene.generic,
    Packages.ray.rage.scene.Light.Type.POINT,
    Packages.java.awt.Color,
    Packages.ray.physics
);

with (JavaPackages) {

    station1PhysicsObject.setBounciness(parseFloat(1.5));
    station2PhysicsObject.setBounciness(parseFloat(1.5));

    station1N.moveForward(parseFloat(60.0));
    station1N.moveUp(parseFloat(12.0));

    station2N.moveBackward(parseFloat(60.0));
    station2N.moveUp(parseFloat(12.0));

    groundPhysicsObject.setBounciness(parseFloat(0.7));
    groundNode.setLocalPosition( parseFloat(0.0), parseFloat(0.0), parseFloat(0.0) );
    groundNode.scale(parseFloat(400), parseFloat(400), parseFloat(400));
    groundNode.moveDown(parseFloat(20.0));
	
}