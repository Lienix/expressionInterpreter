package com.lienisoft.mtr.expressionInterpreter.function;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lienisoft.mtr.expressionInterpreter.basic.IValFactory;

//public class FunctionFactory implements IFunctionFactory<IDataProvider <K, V>> {
public class FuncFactory {
	private static final Logger LOGGER = LoggerFactory.getLogger(FuncFactory.class);

	protected final Map<String, Class<Func>> functionMap;

	// private final IValFactory_NEW valFactory;

	@SuppressWarnings("unchecked")
	public FuncFactory(IValFactory valFactory) {
		// this.valFactory = valFactory;

		functionMap = new HashMap<>();

		// TODO:
		// This does not work in any thinkable environment!!!
		final List<Class<?>> list = getClassesInPackage(getClass().getPackage().getName());
		
		for (final Class<?> clazz : list) {
			final Class<?> superclass = clazz.getSuperclass();

			if (superclass != null) {
				if (Func.class.equals(superclass)) {
					try {
						final Field f = clazz.getDeclaredField("name");
						functionMap.put((String) f.get(null), (Class<Func>) clazz);
					} catch (NoSuchFieldException | SecurityException | IllegalArgumentException
							| IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public void addFunction(String name, Func function) {
		functionMap.put(name, (Class<Func>) function.getClass());
	}

	// @Override
	public Func getFunction(String token) {
		switch (token) {
		case FuncIf.name:
			return new FuncIf();
		case FuncLoop.name:
			return new FuncLoop();
		case FuncGet.name:
			return new FuncGet();
		case FuncGetPat.name:
			return new FuncGetPat();
		case FuncAbs.name:
			return new FuncAbs();
		case FuncLen.name:
			return new FuncLen();
		case FuncRemove.name:
			return new FuncRemove();
		case FuncStartsWith.name:
			return new FuncStartsWith();
		case FunctionContains.name:
			return new FunctionContains();
		case FunctionFind.name:
			return new FunctionFind();
			// case FunctionConvert.name:
		// return new FunctionConvert<>(valFactory);
		default:
			break;
		}

		final Class<Func> clazz = functionMap.get(token);

		if (clazz != null) {
			try {
				return clazz.newInstance();
			} catch (final InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (final IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return null;

		/*
		 *
		 * // log.info("getFunction({})", token);
		 *
		 * switch (token) {
		 * case (FunctionIf.name):
		 * return new FunctionIf();
		 * case (FunctionLen.name):
		 * return new FunctionLen<K, V>();
		 * case (FunctionSub.name):
		 * return new FunctionSub<K, V>();
		 * // case (FunctionRep.name):
		 * // return new FunctionRep();
		 * // case (FunctionReplaceNames.name):
		 * // return new FunctionReplaceNames();
		 * // case (FunctionReplaceValues.name):
		 * // return new FunctionReplaceValues();
		 * // case (FunctionReorder.name):
		 * // return new FunctionReorder();
		 * case (FunctionTrim.name):
		 * return new FunctionTrim<K, V>();
		 * case (FunctionGetPat.name):
		 * return new FunctionGetPat<K, V>();
		 * // case (FunctionFormat.name):
		 * // return new FunctionFormat();
		 * // case (FunctionFreeText.name):
		 * // return new FunctionFreeText();
		 * // case (FunctionConvert.name):
		 * // return new FunctionConvert(valFactory);
		 * case (FunctionAbs.name):
		 * return new FunctionAbs<K, V>();
		 * case (FunctionMod.name):
		 * return new FunctionMod<K, V>();
		 * // case (FunctionNl.name):
		 * // return new FunctionNl();
		 * case (FunctionNow.name):
		 * return new FunctionNow<K, V>();
		 * case (FunctionGet.name):
		 * return new FunctionGet<K, V>();
		 * case (FunctionNvl.name):
		 * return new FunctionNvl<K, V>();
		 * case (FunctionStartsWith.name):
		 * return new FunctionStartsWith<K, V>();
		 * // case (FunctionIsErr.name):
		 * // return new FunctionIsErr();
		 * // case (FunctionErrClass.name):
		 * // return new FunctionErrClass();
		 * // case (FunctionErrCode.name):
		 * // return new FunctionErrCode();
		 * // case (FunctionErrName.name):
		 * // return new FunctionErrName();
		 * case (FunctionPad.name):
		 * return new FunctionPad<K, V>();
		 * case (FunctionSplit.name):
		 * return new FunctionSplit<K, V>();
		 * case (FunctionContains.name):
		 * return new FunctionContains<K, V>();
		 * case (FunctionEncodeBase64.name):
		 * return new FunctionEncodeBase64<K, V>();
		 * case (FunctionDecodeBase64.name):
		 * return new FunctionDecodeBase64<K, V>();
		 * // case (FunctionResponseTxt.name):
		 * // return new FunctionResponseTxt();
		 * case (FunctionIncrement.name):
		 * return new FunctionIncrement<K, V>();
		 * case (FunctionIncrementHex.name):
		 * return new FunctionIncrementHex<K, V>();
		 * // case (FunctionConcat.name):
		 * // return new FunctionConcat();
		 * case (FunctionCompVer.name):
		 * return new FunctionCompVer<K, V>();
		 * case (FunctionBetween.name):
		 * return new FunctionBetween<K, V>();
		 * // case (FunctionNonPrintableRenderer64.name):
		 * // return new FunctionNonPrintableRenderer64();
		 * case (FunctionMatches.name):
		 * return new FunctionMatches<K, V>();
		 * case (FunctionIn.name):
		 * return new FunctionIn<K, V>();
		 * // case (FunctionMappedValue.name):
		 * // return new FunctionMappedValue();
		 * default:
		 * return null;
		 * }
		 */
	}

	public static final List<Class<?>> getClassesInPackage(String packageName) {
		final String path = packageName.replace(".", File.separator);
		final List<Class<?>> classes = new ArrayList<>();
		final String[] classPathEntries = System.getProperty("java.class.path")
				.split(System.getProperty("path.separator"));

		String name;
		for (final String classpathEntry : classPathEntries) {
			if (classpathEntry.endsWith(".jar")) {
				final File jar = new File(classpathEntry);
				try {
					final JarInputStream is = new JarInputStream(new FileInputStream(jar));
					JarEntry entry;
					while ((entry = is.getNextJarEntry()) != null) {
						name = entry.getName();
						if (name.endsWith(".class")) {
							if (name.contains(path) && name.endsWith(".class")) {
								String classPath = name.substring(0, entry.getName().length() - 6);
								classPath = classPath.replaceAll("[\\|/]", ".");
								classes.add(Class.forName(classPath));
							}
						}
					}
					is.close();
				} catch (final Exception ex) {
					// Silence is gold
				}
			} else {
				try {
					final File base = new File(classpathEntry + File.separatorChar + path);
					for (final File file : base.listFiles()) {
						name = file.getName();
						if (name.endsWith(".class")) {
							name = name.substring(0, name.length() - 6);
							classes.add(Class.forName(packageName + "." + name));
						}
					}
				} catch (final Exception ex) {
					// Silence is gold
				}
			}
		}

		return classes;
	}

}
