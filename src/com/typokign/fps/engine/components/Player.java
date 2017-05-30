package com.typokign.fps.engine.components;

import com.bulletphysics.collision.dispatch.PairCachingGhostObject;
import com.bulletphysics.collision.shapes.CapsuleShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.dynamics.character.KinematicCharacterController;
import com.bulletphysics.linearmath.Transform;
import com.typokign.fps.engine.core.CoreEngine;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.math.Vector2f;
import com.typokign.fps.engine.math.Vector3f;
import com.typokign.fps.engine.physics.PhysicsObject;
import com.typokign.fps.engine.rendering.Window;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

/**
 * Created by Typo on 5/28/2017.
 */
public class Player extends GameComponent {
	private boolean mouseLocked = false;
	private final Vector2f centerPosition = new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2);

	private KinematicCharacterController controller;
	private PairCachingGhostObject ghost;

	public Player() {
		controller = new KinematicCharacterController(new PairCachingGhostObject(), new SphereShape(2.5f), 0.1f);
		ghost = new PairCachingGhostObject();
	}

	@Override
	public void addToEngine(CoreEngine engine) {
		super.addToEngine(engine);

		Transform startTransform = new Transform();
		startTransform.setIdentity();
		startTransform.set(new Matrix4f(getRotation().asJavaX(), getPosition().asJavaX(), 1));

		ghost.setWorldTransform(startTransform);
		CapsuleShape capsuleShape = new CapsuleShape(0.5f, 2);
		ghost.setCollisionShape(capsuleShape);

		controller = new KinematicCharacterController(ghost, capsuleShape, 0.35f);
	}

	@Override
	public void input(float delta) {
		float sensitivity = 0.25f;
//		float movAmt = 1000;

		if (Input.getKey(Keyboard.KEY_ESCAPE)) {
			Input.setCursor(true);
			mouseLocked = false;
		}

		if (Input.getMouseDown(0)) {
			Input.setMousePosition(centerPosition);
			Input.setCursor(false);
			mouseLocked = true;
		}

		Transform transform = new Transform();
		ghost.getWorldTransform(transform);

		javax.vecmath.Vector3f forward = new javax.vecmath.Vector3f();
		javax.vecmath.Vector3f up = new javax.vecmath.Vector3f();
		javax.vecmath.Vector3f strafe = new javax.vecmath.Vector3f();

		transform.basis.getRow(2, forward);
		transform.basis.getRow(1, up);
		transform.basis.getRow(0, strafe);

		forward.normalize();
		up.normalize();
		strafe.normalize();

		javax.vecmath.Vector3f walkDirection = new javax.vecmath.Vector3f();

		float walkVelocity = 4.4f * delta;

//		if (Input.getKey(Keyboard.KEY_A)) {
//			Transform trans = new Transform();
//			Matrix3f orientation;
//
//			ghost.getWorldTransform(trans);
//			orientation = trans.basis;
//
//			Matrix3f mat = new Matrix3f();
//			mat.setIdentity();
//
//			orientation.mul(new Matrix3f(new Quat4f(0, 1, 0, 0.01f)));
//
//			new Matrix3f()
//		}

		if (Input.getKey(Keyboard.KEY_W)) {
			walkDirection.scale(walkVelocity);
		}

		controller.setWalkDirection(walkDirection);





//		int factor;

//		if (Input.getKey(Keyboard.KEY_LSHIFT) || Input.getKey(Keyboard.KEY_RSHIFT))
//			factor = 2;
//		else
//			factor = 1;
//
//		if (Input.getKey(Keyboard.KEY_W)) {
//
//			move(getTransform().getRotation().getForward().getXZ(), movAmt * factor);
//		}
//		if (Input.getKey(Keyboard.KEY_S)) {
//			move(getTransform().getRotation().getForward().getXZ(), -movAmt * factor);
//		}
//		if (Input.getKey(Keyboard.KEY_A)) {
//			move(getTransform().getRotation().getLeft().getXZ(), movAmt * factor);
//		}
//		if (Input.getKey(Keyboard.KEY_D)) {
//			move(getTransform().getRotation().getRight().getXZ(), movAmt * factor);
//		}
//		if (Input.getKeyDown(Keyboard.KEY_SPACE)) {
//			jump();
//		}

		if (mouseLocked) {
			Vector2f deltaPos = Input.getMousePosition().sub(centerPosition);

			boolean rotY = deltaPos.getX() != 0;
			boolean rotX = deltaPos.getY() != 0;

			if(rotY)
				getTransform().rotate(new Vector3f(0, 1, 0), (float) Math.toRadians(deltaPos.getX() * sensitivity));
			if(rotX)
				getTransform().rotate(getTransform().getRotation().getRight(), (float) Math.toRadians(-deltaPos.getY() * sensitivity));

			if (rotY || rotX)
				Input.setMousePosition(new Vector2f(Window.getWidth() / 2, Window.getHeight() / 2));
		}
	}

//	public void move(Vector3f direction, float magnitude) {
//		getTransform().setPosition(getTransform().getPosition().add(direction.mul(magnitude)));
//	}
//
//	public void move(Vector2f direction, float magnitude) {
//		Vector2f nDirection = direction.normalized();
//
//		if (!(getParent() instanceof PhysicsObject)) {
//			System.err.println("Player component parent is not kinematic.");
//			new Exception().printStackTrace();
//			System.exit(1);
//		}
//
//		PhysicsObject parent = (PhysicsObject) getParent();
//
//		parent.applyForce(new Vector3f(direction.getX(), 0, direction.getY()).mul(magnitude));
//	}
//
//	public void jump() {
//		System.out.println("j");
//		if (!(getParent() instanceof PhysicsObject)) {
//			System.err.println("Player component parent is not kinematic.");
//			new Exception().printStackTrace();
//			System.exit(1);
//		}
//
//		PhysicsObject parent = (PhysicsObject) getParent();
//
//		parent.applyImpulse(new Vector3f(0, 700, 0));
//	}
}
