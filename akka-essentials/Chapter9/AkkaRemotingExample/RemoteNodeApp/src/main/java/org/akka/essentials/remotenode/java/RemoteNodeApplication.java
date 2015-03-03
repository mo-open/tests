package org.akka.essentials.remotenode.java;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.kernel.Bootable;

import com.typesafe.config.ConfigFactory;

public class RemoteNodeApplication implements Bootable {
	final ActorSystem system = ActorSystem.create("RemoteNodeApp", ConfigFactory
			.load().getConfig("RemoteSys"));

	public void shutdown() {
		system.shutdown();
	}

	public void startup() {
		system.actorOf(Props.create(RemoteActor.class), "remoteActor");
	}
}
