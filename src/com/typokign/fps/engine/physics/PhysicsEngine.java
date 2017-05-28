package com.typokign.fps.engine.physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.typokign.fps.engine.math.Vector3f;

/**
 * Created by Typo on 5/24/2017.
 */
public class PhysicsEngine {

	private DiscreteDynamicsWorld world;

	public PhysicsEngine() {
		BroadphaseInterface broadphase = new DbvtBroadphase();
		DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		CollisionDispatcher collisionDispatcher = new CollisionDispatcher(collisionConfiguration);
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

		world = new DiscreteDynamicsWorld(collisionDispatcher, broadphase, solver, collisionConfiguration);
		world.setGravity(new Vector3f(0, -9.81f, 0).asJavaX());
	}

	public void tick(float delta) {
		world.stepSimulation(delta, 10);
		world.applyGravity();
	}

	public void addRigidBody(RigidBody body) {
		world.addRigidBody(body);
	}
}
