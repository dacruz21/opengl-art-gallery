package com.typokign.fps.engine.physics;

import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.MotionState;
import com.bulletphysics.linearmath.Transform;
import com.typokign.fps.engine.core.CoreEngine;
import com.typokign.fps.engine.core.GameObject;
import com.typokign.fps.engine.core.Input;
import com.typokign.fps.engine.math.Vector3f;
import org.lwjgl.input.Keyboard;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;

/**
 * Created by Typo on 5/27/2017.
 */
public class PhysicsObject extends GameObject {
	private CollisionShape collisionShape;
	private MotionState motionState;
	private RigidBodyConstructionInfo constructionInfo;
	private RigidBody rigidBody;

	private float mass = 0f;

	public PhysicsObject(CollisionShape collisionShape, float mass) {
		super();
		this.collisionShape = collisionShape;
		motionState = new DefaultMotionState();
		this.mass = mass;

		javax.vecmath.Vector3f localInertia = new javax.vecmath.Vector3f(0, 0, 0);
		collisionShape.calculateLocalInertia(mass, localInertia);
		constructionInfo = new RigidBodyConstructionInfo(mass, motionState, collisionShape, localInertia);

		rigidBody = new RigidBody(constructionInfo);
	}

	public CollisionShape getCollisionShape() {
		return collisionShape;
	}

	public void setCollisionShape(CollisionShape collisionShape) {
		this.collisionShape = collisionShape;
	}

	public MotionState getMotionState() {
		return motionState;
	}

	public RigidBody getRigidBody() {
		return rigidBody;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public float getMass() {
		return mass;
	}

	public void setRestitution(float restitution) {
		rigidBody.setRestitution(restitution);
	}

	public float getRestitution() {
		return rigidBody.getRestitution();
	}

	public void setFriction(float friction) {
		rigidBody.setFriction(friction);
	}

	public float getFriction() {
		return rigidBody.getFriction();
	}

	@Override
	public void setEngine(CoreEngine engine) {
		super.setEngine(engine);
	}

	@Override
	public void update(float delta) {
		super.update(delta);

		if (rigidBody != null) {
			Transform transform = new Transform();
			rigidBody.getWorldTransform(transform);

			javax.vecmath.Vector3f velocity = new javax.vecmath.Vector3f();
			rigidBody.getVelocityInLocalPoint(new javax.vecmath.Vector3f(0, 0, 0), velocity);

			if (Input.getKey(Keyboard.KEY_U)) {
				rigidBody.applyCentralForce(new javax.vecmath.Vector3f(0, 0, 0));
			}

			getPosition().set(transform.origin.x, transform.origin.y, transform.origin.z);
		}

		rigidBody.getCollisionFlags();
	}

	public PhysicsObject setPosition(Vector3f position) {
		motionState.setWorldTransform(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1),
				position.asJavaX(),
				1.0f)));

		super.setPosition(position);

		return this;
	}

	public PhysicsObject setPosition(float x, float y, float z) {
		return setPosition(new Vector3f(x, y, z));
	}

	public void applyForce(Vector3f force, Vector3f relativePosition) {
		rigidBody.applyForce(force.asJavaX(), relativePosition.asJavaX());
	}

	public void applyForce(Vector3f force) {
		rigidBody.applyCentralForce(force.asJavaX());
	}

	public void applyImpulse(Vector3f impulse, Vector3f relativePosition) {
		rigidBody.applyImpulse(impulse.asJavaX(), relativePosition.asJavaX());
	}

	public void applyImpulse(Vector3f impulse) {
		rigidBody.applyCentralImpulse(impulse.asJavaX());
	}
}
