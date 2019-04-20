package com.practicaldime.jetty.callback.example;

import io.reactivex.Emitter;
import io.reactivex.functions.Consumer;

public abstract class ToFlowable implements Consumer<IntEvent>{

	protected Emitter<IntEvent> emitter;

	public void setEmitter(Emitter<IntEvent> emitter) {
		this.emitter = emitter;
	}
}
