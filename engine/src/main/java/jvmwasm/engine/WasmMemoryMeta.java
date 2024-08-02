package jvmwasm.engine;

import jvmwasm.format.data.V128;
import jvmwasm.format.types.MemType;
import jvmwasm.format.types.Limits;

final class WasmMemoryMeta implements WasmMemory {

	public WasmMemoryMeta(Engine engine, Integer maxSize, WasmMemoryNoResize mem) {
		this.engine = engine;
		this.maxSize = maxSize;
		this.mem = mem;
	}

	private final Engine engine;
	private final Integer maxSize;
	private WasmMemoryNoResize mem;

	@Override
	public MemType type() {
		return new MemType(new Limits(mem.pageSize(), maxSize));
	}

	@Override
	public int byteSize() {
		return mem.byteSize();
	}

	@Override
	public int pageSize() {
		return mem.pageSize();
	}

	@Override
	public byte loadI8(int address) throws Throwable {
		return mem.loadI8(address);
	}

	@Override
	public short loadI16(int address) throws Throwable {
		return mem.loadI16(address);
	}

	@Override
	public int loadI32(int address) throws Throwable {
		return mem.loadI32(address);
	}

	@Override
	public long loadI64(int address) throws Throwable {
		return mem.loadI64(address);
	}

	@Override
	public float loadF32(int address) throws Throwable {
		return mem.loadF32(address);
	}

	@Override
	public double loadF64(int address) throws Throwable {
		return mem.loadF64(address);
	}

	@Override
	public V128 loadV128(int address) throws Throwable {
		return mem.loadV128(address);
	}

	@Override
	public void storeI8(int address, byte value) throws Throwable {
		mem.storeI8(address, value);
	}

	@Override
	public void storeI16(int address, short value) throws Throwable {
		mem.storeI16(address, value);
	}

	@Override
	public void storeI32(int address, int value) throws Throwable {
		mem.storeI32(address, value);
	}

	@Override
	public void storeI64(int address, long value) throws Throwable {
		mem.storeI64(address, value);
	}

	@Override
	public void storeF32(int address, float value) throws Throwable {
		mem.storeF32(address, value);
	}

	@Override
	public void storeF64(int address, double value) throws Throwable {
		mem.storeF64(address, value);
	}


	@Override
	public int grow(int pages) throws Throwable {
		int oldPages = mem.pageSize();
		var newPages = oldPages + pages;
		if(pages < 0 || newPages < 0 || (maxSize != null && maxSize < newPages)) {
			return -1;
		}

		int engineMemoryLimit = engine.getMaxMemory();
		if(engineMemoryLimit > 0 && newPages > engineMemoryLimit) {
			return -1;
		}

		var newMem = engine.allocateMemory(newPages);
		for(int address = 0; address < mem.byteSize(); address += 8) {
			newMem.storeI64(address, mem.loadI64(address));
		}

		mem = newMem;
		return oldPages;
	}
}
