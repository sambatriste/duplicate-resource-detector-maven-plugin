package com.github.sambatriste.drd.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * 値に複数の要素を持つMapのラッパークラス。
 *
 * @param <K> キーの型
 * @param <V> 値の型
 */
public class MultiValueMapWrapper<K, V> {

    /** ラップ対象のMap */
    private final Map<K, Collection<V>> map;

    /** コンストラクタ。*/
    public MultiValueMapWrapper() {
        this(new LinkedHashMap<K, Collection<V>>());
    }

    /**
     * コンストラクタ。
     *
     * @param map マップ
     */
    public MultiValueMapWrapper(Map<K, Collection<V>> map) {
        this.map = map;
    }

    /**
     * ラップしているMapインスタンスを取得する。
     *
     * @return Map
     */
    public Map<K, Collection<V>> unwrap() {
        return map;
    }

    /**
     * 重複したリソースを登録する。
     *
     * @param key         キー
     * @param valuesToAdd 追加対象の値
     */
    public void add(K key, V[] valuesToAdd) {
        add(key, Arrays.asList(valuesToAdd));
    }

    /**
     * 重複したリソースを登録する。
     *
     * @param key         キー
     * @param valuesToAdd 追加対象の値
     */
    public void add(K key, Collection<V> valuesToAdd) {
        Collection<V> container = getValueContainerOf(key);
        container.addAll(valuesToAdd);
    }

    /**
     * キー対応する値のコレクションを取得する。
     *
     * @param key リソースパス
     * @return リソースパスに対応するクラスパスのSet
     */
    private Collection<V> getValueContainerOf(K key) {
        Collection<V> container = map.get(key);
        if (container == null) {
            container = createValueContainer();
            map.put(key, container);
        }
        return container;
    }

    /**
     * 複数の値を格納するためのコレクションを生成する。
     *
     * @return コレクション
     */
    protected Collection<V> createValueContainer() {
        return new LinkedHashSet<>();
    }
}
